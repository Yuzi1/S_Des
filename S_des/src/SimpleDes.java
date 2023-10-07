import java.util.Arrays;

public class SimpleDes {
    String key;

    //密钥抗战
    static int[] P10 ={3,5,2,7,4,10,1,9,8,6};
    static int[] P8 ={6,3,7,4,8,5,10,9};
    static int[] Leftshift1 = {2,3,4,5,1};
    static int[] LeftShift2 = {3,4,5,1,2};
    static int[] IP = {2,6,3,1,4,8,5,7};
    static int[] IP_1 = {4,1,3,5,7,2,8,6};
    static int[] EPBox = {4, 1, 2, 3, 2, 3, 4, 1};
    static int[][] SBox1 = {
            {1, 0, 3, 2},
            {3, 2, 1, 0},
            {0, 2, 1, 3},
            {3, 1, 0, 2}
    };
    static int[][] SBox2 = {
            {0, 1, 2, 3},
            {2, 3, 1, 0},
            {3, 0, 1, 2},
            {2, 1, 0, 3}
    };

    static int[] SPBox = {2,4,3,1};
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
        int[] shiftedsubkey = new int[subkey.length];

        for (int i = 0; i < subkey.length; i++) {
            int temp = (i + rule[i % rule.length]) % subkey.length; //当前位左移后的目标位置
            shiftedsubkey[temp] = subkey[i];
        }

        return shiftedsubkey;
    }

    //子密钥生成函数
    private static int[] KeyExpansion(int[] key,int i){
        int[] key_p10 = Permute(key,P10);

        //将密钥分为左半部分和右半部分
        int[] leftKey = Arrays.copyOfRange(key_p10,0,5);
        int[] rightKey = Arrays.copyOfRange(key_p10,5,10);

        // 根据轮次选择LeftShift1 or LeftShift2
        int[] rule = (i == 1) ? Leftshift1 : LeftShift2;

        //进行左移操作
        leftKey = leftShift(leftKey,rule);
        rightKey = leftShift(rightKey,rule);

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
        int[] IPResult = new int[8];

        for (int i = 0; i < 8; i++) {
            IPResult[i] = plaintext[IP[i] - 1];
        }

        return IPResult;
    }
    //最终置换
    public static int[] finalPermutation(int[] text) {
        int[] IPIResult = new int[8];

        for (int i = 0; i < 8; i++) {
            IPIResult[i] = text[IP_1[i] - 1];
        }

        return IPIResult;
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

        //转为4位二进制
        int[] Bits = new int[4];
        for (int i = 0; i < 4; i++){
            Bits[3 - i] = (num >> i) & 1;
        }
        return  Bits;
    }
    //fk函数
    public static int[] Fk(int[] left, int[] right, int[] subkey){
        // 计算 F 函数的输出
        int[] FResult = F(right,subkey);
        // 将F 函数的输出结果与左半部分进行异或操作
        int[] FkResult = exclusiveOR(left,FResult);
        return FkResult;
    }


    //交换函数
    public static void swap(int[] array1, int[] array2) {
        if (array1.length != array2.length) {
            throw new IllegalArgumentException("长度不同.");
        }

        int[] temp = new int[array1.length];
        System.arraycopy(array1, 0, temp, 0, array1.length);
        System.arraycopy(array2, 0, array1, 0, array1.length);
        System.arraycopy(temp, 0, array2, 0, temp.length);
    }

    //加密
    public static  int[] encrypt(int[] plaintext, int[] key){
        //初始置换
        int[] IPresult = initialPermutation(plaintext);

        //拆分为左半部分和右半部分
        int[] leftBlock = Arrays.copyOfRange(IPresult,0,4);
        int[] rightBlock = Arrays.copyOfRange(IPresult,4,8);

        //密钥k1、k2生成
        int[] k1 = KeyExpansion(key,1);
        int[] k2 = KeyExpansion(key,2);

        //第一轮加密
        int[] output1 = Fk(leftBlock,rightBlock,k1);

        swap(leftBlock,rightBlock);

        //第二轮加密
        int[] output2 = Fk(leftBlock,rightBlock,k2);

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
        // 初始置换 IP
        int[] Ipresult = initialPermutation(ciphertext);

        //拆分为左半部分和右半部分
        int[] leftBlock = Arrays.copyOfRange(Ipresult, 0, 4);
        int[] rightBlock = Arrays.copyOfRange(Ipresult, 4, 8);

        //生成子密钥 K2 、 K1
        int[] k1 = KeyExpansion(key, 2);
        int[] k2 = KeyExpansion(key, 1);

        //第一轮解密
        int[] output1 = Fk(leftBlock, rightBlock, k2);
        swap(leftBlock, rightBlock);

        //第二轮解密
        int[] output2 = Fk(leftBlock, rightBlock, k1);

        //合并
        int[] result = new int[8];
        System.arraycopy(leftBlock, 0, result, 0, 4);
        System.arraycopy(rightBlock, 0, result, 4, 4);

        //最终置换
        int[] plaintext = finalPermutation(result);

        return plaintext;
    }
}
