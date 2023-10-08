# S_Des
使用JAVA+Swing实现S-Des的加密、解密，并且提供GUI界面支持用户交互
1.S_des的算法核心代码如下：
//置换函数
    public static int[] Permute(int[] in,int[] p){

        int[] result = new int[p.length];
        for(int i = 0; i < p.length; i++){
            result[i] = in[p[i] - 1];

        }
        return result;
    }


    //左移函数
    private static int[] leftShift(int[] subkey, int[] rule) {

        return Permute(subkey,rule);
    }

    //子密钥生成函数
    private static int[] KeyExpansion(int[] key,int i){
        int[] key_p10 = Permute(key,P10);

        System.out.println(Arrays.toString(key_p10));
        //将密钥分为左半部分和右半部分
        int[] leftKey = Arrays.copyOfRange(key_p10,0,5);
        int[] rightKey = Arrays.copyOfRange(key_p10,5,10);

        // 根据轮次选择LeftShift1 or LeftShift2
        int[] rule = (i == 1) ? Leftshift1 : LeftShift2;

        //进行左移操作
        leftKey = leftShift(leftKey,rule);
        rightKey = leftShift(rightKey,rule);

        System.out.println(Arrays.toString(leftKey));
        System.out.println(Arrays.toString(rightKey));
        //合并密钥
        int[] newKey= new int[10];
        System.arraycopy(leftKey, 0, newKey, 0, 5);
        //将右半部分的5位复制到 newKey 数组的后半部分末尾，实现了左半部分和右半部分的合并
        System.arraycopy(rightKey,0,newKey,5,5);

        //置换为8位子密钥
        return Permute(newKey,P8);
    }

    //初始置换
    public static int[] initialPermutation(int[] plaintext) {
        return Permute(plaintext, IP);
    }
    //最终置换
    public static int[] finalPermutation(int[] text) {
        return Permute(text, IP_1);
    }
    //轮函数F
    public static int[] F(int[] right, int[] subkey){
        //扩展置换 (EPBox)
        int[] Rightexpansion = Permute(right,EPBox);

        //异或运算,加轮密钥
        int[] xorResult = exclusiveOR(Rightexpansion,subkey);

        //拆分,2个4位块
        int[] leftPart = Arrays.copyOfRange(xorResult,0,4);
        int[] rightPart = Arrays.copyOfRange(xorResult,4,8);

        //替换盒SBox
        int[] leftSubstitue = Substitue(leftPart,SBox1);
        int[] rightSubstitue = Substitue(rightPart,SBox2);

        //合并置换后的结果,4bits
        int[] SBoxResult = new int[4];
        System.arraycopy(leftSubstitue,0,SBoxResult,0,2);
        System.arraycopy(rightSubstitue,0,SBoxResult,2,2);

        //直接置换SPBox
        int[] output = Permute(SBoxResult,SPBox);

        return output;
    }

    //异或函数
    public static int[] exclusiveOR(int[] array1, int[] array2){

     int[] result = new int[array1.length];
        for (int i = 0; i < array1.length; i++){
            result[i] = array1[i] ^ array2[i];

        }
        return result;
    }

    // //替换盒SBox函数
    public static int[] Substitue(int[] part, int[][] sbox){
        int row = part[0] * 2 + part[3];   //SBox的行索引
        int column = part[1] * 2 + part[2]; //SBox的列索引

        //查找SBox中对于的值
        int num = sbox[row][column];

        // 直接将SBox的输出作为结果
        int[] Bits = new int[2];
        Bits[0] = (num >> 1) & 1;
        Bits[1] = num & 1;

        return  Bits;
    }
    //加密的fk函数
    public static int[] Fk(int[] left, int[] right, int[] subkey){
        // 计算 F 函数的输出
        int[] FResult = F(right,subkey);

        // 将F 函数的输出结果与左半部分进行异或操作
        int[] FkResult = exclusiveOR(left,FResult);

        return FkResult;
    }


    //交换函数
    public static int[][] swap(int[] array1, int[] array2) {
        if (array1.length != array2.length) {
            throw new IllegalArgumentException("数组长度不同.");
        }

        int[] temp = new int[array1.length];
        System.arraycopy(array1, 0, temp, 0, array1.length);
        System.arraycopy(array2, 0, array1, 0, array1.length);
        System.arraycopy(temp, 0, array2, 0, temp.length);

        return new int[][] {array2, array1}; // 返回交换后的数组
    }


    //加密
    public static  int[] encrypt(int[] plaintext, int[] key){
        //初始置换
        int[] IPresult = initialPermutation(plaintext);


        //拆分为左半部分和右半部分
        int[] leftBlock = Arrays.copyOfRange(IPresult,0,4);
        int[] rightBlock = Arrays.copyOfRange(IPresult,4,8);


        //密钥k1、k2生成
        // 密钥生成
        int[] k1 = KeyExpansion(key, 1);
        int[] k2 = KeyExpansion(key, 2);


        //第一轮加密
        int[] output1 = Fk(leftBlock,rightBlock,k1);
        leftBlock = output1;

        int[][] swappedArrays = swap(leftBlock, rightBlock);
        leftBlock = swappedArrays[1];
        rightBlock = swappedArrays[0];

        //第二轮加密
        int[] output2 = Fk(leftBlock,rightBlock,k2);
        leftBlock = output2;

        //合并
        int[] result = new int[8];
        System.arraycopy(leftBlock,0,result,0,4);
        System.arraycopy(rightBlock, 0 , result,4,4);

        //最终置换
        int[] ciphertext = finalPermutation(result);


        return ciphertext;
    }

    //解密
    public static int[] decrypt(int[] ciphertext, int[] key) {
        // 初始置换
        int[] IPIResult = initialPermutation(ciphertext);

        // 拆分为左半部分和右半部分
        int[] leftBlock = Arrays.copyOfRange(IPIResult, 0, 4);
        int[] rightBlock = Arrays.copyOfRange(IPIResult, 4, 8);

        // 生成子密钥 K2 、 K1
        int[] k1 = KeyExpansion(key, 1);
        int[] k2 = KeyExpansion(key, 2);

        // 第一轮解密
        int[] output1 = Fk(leftBlock, rightBlock, k2);
        leftBlock = output1;

        int[][] swappedArrays = swap(leftBlock, rightBlock);
        leftBlock = swappedArrays[1];
        rightBlock = swappedArrays[0];

        // 第二轮解密
        int[] output2 = Fk(leftBlock, rightBlock, k1);

        leftBlock = output2;


        // 合并
        int[] result = new int[8];
        System.arraycopy(leftBlock, 0, result, 0, 4);
        System.arraycopy(rightBlock, 0, result, 4, 4);

        // 最终置换
        int[] plaintext = finalPermutation(result);

        return plaintext;
    
2.测试
（1）基本测试
GUI首页界面（S_desWindow.java）：

<img width="291" alt="index" src="https://github.com/Yuzi1/S_Des/assets/94826086/62aa4be0-a4e6-42c9-98c9-62dd1c9d5f34">

加密：

![jiami1](https://github.com/Yuzi1/S_Des/assets/94826086/a1b415a9-ef57-4a32-8c4f-cd2d84f48643)

加密界面代码：EncryptWindow.java
解密：

![jiemi1](https://github.com/Yuzi1/S_Des/assets/94826086/6934a85c-8b07-4eb1-918a-a0deeb1cdf5d)

解密界面代码:DecryptWindow.java
加密和解密的结果相符。

（2）交叉测试

![othergroup](https://github.com/Yuzi1/S_Des/assets/94826086/6aa5004b-76de-443c-929b-8d5b2f694fc0)
我的解密结果：

![myresult](https://github.com/Yuzi1/S_Des/assets/94826086/6aec1f38-579c-4560-8284-dbf8620a1001)

解密的明文相同。

（3）扩展功能
增加输入的数据类型选择

<img width="291" alt="jiami2" src="https://github.com/Yuzi1/S_Des/assets/94826086/82dacae0-8926-4e10-a276-0650f9b83760">

<img width="291" alt="jiemi2" src="https://github.com/Yuzi1/S_Des/assets/94826086/6f636439-8a3f-4666-9752-ec5f69934a16">

输入ASCII字符：

<img width="291" alt="Ascii" src="https://github.com/Yuzi1/S_Des/assets/94826086/4f2f5f2c-6c61-4c71-943e-2ffafc368526">

（4）暴力破解
多线程破解代码如下：

<img width="422" alt="force1" src="https://github.com/Yuzi1/S_Des/assets/94826086/fa7958e7-940b-4a44-bd52-b952106d47df">
<img width="379" alt="force2" src="https://github.com/Yuzi1/S_Des/assets/94826086/0bb0a22f-0d28-4461-bd8c-1ce8f9fb4e2a">

运行：

https://github.com/Yuzi1/S_Des/assets/94826086/8ede200a-41c1-4b27-9eed-1f22d05536e0

（5)封闭测试
当只有一对明、密文对时，
明文：10101010        密文：00011011
暴力破解的密钥：1000011001

<img width="293" alt="forcekey" src="https://github.com/Yuzi1/S_Des/assets/94826086/8506e93f-4c09-40d5-b62e-561a7e693918">

密钥为：
但是交互测试中相同的明、密文对，密钥为：1111100000

![myresult](https://github.com/Yuzi1/S_Des/assets/94826086/8a07c60a-4eff-4357-93e9-8afe5ebc9149)

这个S-Des的密钥空间相对较小，只有1024个可能的密钥。在明文空间较大的情况下，可能存在多个不同的密钥，但生成相同的密文。





