import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Knn {
    public static void main(String[] args){
        try{
            //读取训练集
            List<int[]> trainVects = new ArrayList();		//训练集特征向量
            List<Integer> trainLabels = new ArrayList();	//训练集Label
            readDataSet("src/ceshiji/train.csv", trainVects, trainLabels);
            //读取测试集
            List<int[]> testVects = new ArrayList();		//测试集特征向量
            List<Integer> testLabels = new ArrayList();		//测试集Label
            readDataSet("src/ceshiji/test_200.csv", testVects, testLabels);

            double k =1900;        //k值
            double success=0;                                   //成功次数

            for(int i=0;i<testVects.size();i++){               //遍历测试集
                int[] testVect=testVects.get(i);            //获取测试集的一行
                List<Integer>labels = new ArrayList<>();        //保存符合要求的label
                List<Integer>count = new ArrayList<>();         //保存每个label出现次数
                int mostLabel;                                   //出现次数最多的label
                double distance;                            //distance
                double sum;
                for(int j=0;j<trainVects.size();j++){           //遍历训练集，使得测试集的任一项与训练集项求distance
                    sum = 0;                                    //差的平方和
                    int[] trainVect = trainVects.get(j);        //获取训练集的一行
                    for(int m=0;m<784;m++){                     //遍历一个向量内的每一维
                        double x = (double)(testVect[m]-trainVect[m]);
                        sum+=Math.pow(x,2);//求差的平方和
                    }
                    distance = Math.sqrt(sum);
                    if(distance<k) {

                        int n=labels.indexOf(trainLabels.get(j));
                        if (n!=-1){
                            int newCount = count.get(n)+1;
                            count.set(n,newCount);
                        }
                        else {
                            count.add(0);
                            labels.add(trainLabels.get(j));
                        }
                    }

                }
                if(count.size()!=0){
                    int mostCount = Collections.max(count);
                    mostLabel = labels.get(count.indexOf(mostCount));
                    if(mostLabel==testLabels.get(i)){
                        success++;
                    }
                    count.clear();
                    labels.clear();
                }

            }
            System.out.println(success/testLabels.size());

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //读取特征向量和label
    private static void readDataSet(String path, List<int[]> vects, List<Integer> labels) throws Exception{
        //数据集文件名
        File trainFile = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(trainFile));
        String line = "";
        br.readLine();  //忽略表头
        //遍历所有行
        while ((line = br.readLine()) != null) {   //使用readLine方法，一次读一行
            String[] items = line.split(",");
            int len = items.length;
            int[] vect = new int[len - 1];
            //读取第一列，写入label
            labels.add(Integer.parseInt(items[0]));
            //遍历后续列，写入特征向量
            for(int i = 1; i < len; i ++){
                vect[i - 1] = Integer.parseInt(items[i]);
            }
            vects.add(vect);
        }
        br.close();
    }
}
