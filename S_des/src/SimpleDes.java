import java.util.Arrays;

public class SimpleDes {
    String key;

    //��Կ��ս
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
    //�û�����
    public static int[] Permute(int[] in,int[] p){

        int[] result = new int[p.length];
        for(int i = 0; i < p.length; i++){
            result[i] = in[p[i] - 1];
        }
        return result;
    }


    //���ƺ���
    private static int[] leftShift(int[] subkey, int[] rule) {
        int[] shiftedsubkey = new int[subkey.length];

        for (int i = 0; i < subkey.length; i++) {
            int temp = (i + rule[i % rule.length]) % subkey.length; //��ǰλ���ƺ��Ŀ��λ��
            shiftedsubkey[temp] = subkey[i];
        }

        return shiftedsubkey;
    }

    //����Կ���ɺ���
    private static int[] KeyExpansion(int[] key,int i){
        int[] key_p10 = Permute(key,P10);

        //����Կ��Ϊ��벿�ֺ��Ұ벿��
        int[] leftKey = Arrays.copyOfRange(key_p10,0,5);
        int[] rightKey = Arrays.copyOfRange(key_p10,5,10);

        // �����ִ�ѡ��LeftShift1 or LeftShift2
        int[] rule = (i == 1) ? Leftshift1 : LeftShift2;

        //�������Ʋ���
        leftKey = leftShift(leftKey,rule);
        rightKey = leftShift(rightKey,rule);

        //�ϲ���Կ
        int[] newKey= new int[10];
        System.arraycopy(leftKey, 0, newKey, 0, 5);
        //���Ұ벿�ֵ�5λ���Ƶ� newKey ����ĺ�벿��ĩβ��ʵ������벿�ֺ��Ұ벿�ֵĺϲ�
        System.arraycopy(rightKey,0,newKey,5,5);
        //�û�Ϊ8λ����Կ
        return Permute(newKey,P8);
    }

    //��ʼ�û�
    public static int[] initialPermutation(int[] plaintext) {
        int[] IPResult = new int[8];

        for (int i = 0; i < 8; i++) {
            IPResult[i] = plaintext[IP[i] - 1];
        }

        return IPResult;
    }
    //�����û�
    public static int[] finalPermutation(int[] text) {
        int[] IPIResult = new int[8];

        for (int i = 0; i < 8; i++) {
            IPIResult[i] = text[IP_1[i] - 1];
        }

        return IPIResult;
    }
    //�ֺ���F
    public static int[] F(int[] right, int[] subkey){
        //��չ�û� (EPBox)
        int[] Rightexpansion = Permute(right,EPBox);

        //�������,������Կ
        int[] xorResult = exclusiveOR(Rightexpansion,subkey);

        //���,2��4λ��
        int[] leftPart = Arrays.copyOfRange(xorResult,0,4);
        int[] rightPart = Arrays.copyOfRange(xorResult,4,8);

        //�滻��SBox
        int[] leftSubstitue = Substitue(leftPart,SBox1);
        int[] rightSubstitue = Substitue(rightPart,SBox2);

        //�ϲ��û���Ľ��,4bits
        int[] SBoxResult = new int[4];
        System.arraycopy(leftSubstitue,0,SBoxResult,0,2);
        System.arraycopy(rightSubstitue,0,SBoxResult,2,2);

        //ֱ���û�SPBox
        int[] output = Permute(SBoxResult,SPBox);

        return output;
    }

    //�����
    public static int[] exclusiveOR(int[] array1, int[] array2){
        int[] result = new int[array1.length];
        for (int i = 0; i < array1.length; i++){
            result[i] = array1[i] ^ array2[i];
        }
        return result;
    }

    // //�滻��SBox����
    public static int[] Substitue(int[] part, int[][] sbox){
        int row = part[0] * 2 + part[3];   //SBox��������
        int column = part[1] * 2 + part[2]; //SBox��������

        //����SBox�ж��ڵ�ֵ
        int num = sbox[row][column];

        //תΪ4λ������
        int[] Bits = new int[4];
        for (int i = 0; i < 4; i++){
            Bits[3 - i] = (num >> i) & 1;
        }
        return  Bits;
    }
    //fk����
    public static int[] Fk(int[] left, int[] right, int[] subkey){
        // ���� F ���������
        int[] FResult = F(right,subkey);
        // ��F ����������������벿�ֽ���������
        int[] FkResult = exclusiveOR(left,FResult);
        return FkResult;
    }


    //��������
    public static void swap(int[] array1, int[] array2) {
        if (array1.length != array2.length) {
            throw new IllegalArgumentException("���Ȳ�ͬ.");
        }

        int[] temp = new int[array1.length];
        System.arraycopy(array1, 0, temp, 0, array1.length);
        System.arraycopy(array2, 0, array1, 0, array1.length);
        System.arraycopy(temp, 0, array2, 0, temp.length);
    }

    //����
    public static  int[] encrypt(int[] plaintext, int[] key){
        //��ʼ�û�
        int[] IPresult = initialPermutation(plaintext);

        //���Ϊ��벿�ֺ��Ұ벿��
        int[] leftBlock = Arrays.copyOfRange(IPresult,0,4);
        int[] rightBlock = Arrays.copyOfRange(IPresult,4,8);

        //��Կk1��k2����
        int[] k1 = KeyExpansion(key,1);
        int[] k2 = KeyExpansion(key,2);

        //��һ�ּ���
        int[] output1 = Fk(leftBlock,rightBlock,k1);

        swap(leftBlock,rightBlock);

        //�ڶ��ּ���
        int[] output2 = Fk(leftBlock,rightBlock,k2);

        //�ϲ�
        int[] result = new int[8];
        System.arraycopy(leftBlock,0,result,0,4);
        System.arraycopy(rightBlock, 0 , result,4,4);

        //�����û�
        int[] ciphertext = finalPermutation(result);

        return ciphertext;
    }
    //����
    public static int[] decrypt(int[] ciphertext, int[] key) {
        // ��ʼ�û� IP
        int[] Ipresult = initialPermutation(ciphertext);

        //���Ϊ��벿�ֺ��Ұ벿��
        int[] leftBlock = Arrays.copyOfRange(Ipresult, 0, 4);
        int[] rightBlock = Arrays.copyOfRange(Ipresult, 4, 8);

        //��������Կ K2 �� K1
        int[] k1 = KeyExpansion(key, 2);
        int[] k2 = KeyExpansion(key, 1);

        //��һ�ֽ���
        int[] output1 = Fk(leftBlock, rightBlock, k2);
        swap(leftBlock, rightBlock);

        //�ڶ��ֽ���
        int[] output2 = Fk(leftBlock, rightBlock, k1);

        //�ϲ�
        int[] result = new int[8];
        System.arraycopy(leftBlock, 0, result, 0, 4);
        System.arraycopy(rightBlock, 0, result, 4, 4);

        //�����û�
        int[] plaintext = finalPermutation(result);

        return plaintext;
    }
}
