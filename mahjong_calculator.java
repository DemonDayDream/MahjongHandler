/*
 * @author:demondaydream
 * 
 * ���ƣ��齫���Ƽ�����
 * 
 * ���ã�����һ��13�����ͣ�m��p��s��z˳�򣩣������������
 * 1-9m��1-9p��1-9s��0��5���㣩
 * 1-7z�����������׷���
 * 
 * ����ٿ����߶Ժ͹�ʿ
 */
import java.util.*;
public class mahjong_calculator 
{
	public static int[] cnt=new int[34];     //�ֱ�洢34���Ƶ��������
	
	public static int[] yaojiu=new int[13];
	
	public static String[] idx= {"1m","2m","3m","4m","5m","6m","7m","8m","9m",
			                     "1p","2p","3p","4p","5p","6p","7p","8p","9p",
			                     "1s","2s","3s","4s","5s","6s","7s","8s","9s",
			                     "1z","2z","3z","4z","5z","6z","7z"};
	
	public static boolean[] tingpai=new boolean[34];//�Ƿ�Ϊ���ƣ���idx��Ӧ��ʾ
	
	public static boolean jiang=false;//�Ƿ��Ѿ�����ȸͷ������
	
	public static int[] yupai=new int[4];//������ȡ���������Ӻ��������
	
	public static void main(String[] args) 
	{
		Scanner sc=new Scanner(System.in);
		System.out.println("���������ͣ�");
		String s=sc.next();
		//System.out.println(s);     //�������Ͷ���
		String[] box=new String[4];//�ֱ�洢m��p��s��z���
		for(int i=0;i<=3;i++)
		{
			box[i]=new String("");
		}
		
		int index=-1;             //λ�ñ���
		if(s.contains("m"))
		{
			index=s.indexOf('m');
			box[0]=s.substring(0,index);
		}
		if(s.contains("p"))
		{
			int cur=s.indexOf('p');
			box[1]=s.substring(index==-1?0:index+1,cur);
			index=cur;
		}
		if(s.contains("s"))
		{
			int cur=s.indexOf('s');
			box[2]=s.substring(index==-1?0:index+1,cur);
			index=cur;
		}
		if(s.contains("z"))
		{
			int cur=s.indexOf('z');
			box[3]=s.substring(index==-1?0:index+1,cur);
			index=cur;
		}
		/*
		System.out.println(box[0]);//test for m
		System.out.println(box[1]);//test for p
		System.out.println(box[2]);//test for s
		System.out.println(box[3]);//test for z
		*/
		
		//ÿ�������������cnt
		for(int i=0;i<=3;i++)
		{
			String temp=box[i];
			for(int j=0;j<=temp.length()-1;j++)
			{
				int n=temp.charAt(j)-'0';
				cnt[9*i+(n-1)]++;
			}
		}
		
		//test for num of each pai
		/*
		for(int i=0;i<=8;i++)System.out.print(cnt[i]+" ");
		System.out.println();
		for(int i=9;i<=17;i++)System.out.print(cnt[i]+" ");
		System.out.println();
		for(int i=18;i<=26;i++)System.out.print(cnt[i]+" ");
		System.out.println();
		for(int i=27;i<=33;i++)System.out.print(cnt[i]+" ");
		System.out.println();
		*/
		
		//������������13�� ������
		int sum=0;
		for(int num:cnt)sum+=num;
		if(sum!=13)
		{
			System.out.println("��������������13���ƣ�");
			return;
		}
		
		//int suc=0;//�������͵Ĵ��ӣ����һ�����������Ҳ�����߶Թ�ʿ����Ϊδ���ƣ���һ����Ӳ��㣩
		/*for(int i=0;i<=3;i++)
		{
			String str=s;
			if(str.length()==0)continue;
			suc=calc_tingpai();//����֮ǰ��cnt����������ӣ�ͬʱö���ж����ƣ�����tingpai����
		}*/
		calc_tingpai();
		boolean flag=false;
		for(boolean ting:tingpai)if(ting) {flag=true;break;}
		if(!flag)//�޷��γɳ���5�����ӣ���һȸͷ��ʱ�������Ƿ��Ƿǳ�������
		{
			if(guoshi13())
			{
				System.out.println("���ƣ�");
				System.out.println("����Ϊ��1m 9m 1p 9p 1s 9s 1z 2z 3z 4z 5z 6z 7z");
				return;
			}
			if(guoshi())
			{
				System.out.println("���ƣ�");
				System.out.print("����Ϊ��");
				for(int i=0;i<=12;i++)
				{
					if(yaojiu[i]==0)
					{
						if(i==0)System.out.println("1m");
						if(i==1)System.out.println("9m");
						if(i==2)System.out.println("1p");
						if(i==3)System.out.println("9p");
						if(i==4)System.out.println("1s");
						if(i==5)System.out.println("9s");
						if(i==6)System.out.println("1z");
						if(i==7)System.out.println("2z");
						if(i==8)System.out.println("3z");
						if(i==9)System.out.println("4z");
						if(i==10)System.out.println("5z");
						if(i==11)System.out.println("6z");
						if(i==12)System.out.println("7z");
						break;
					}
				}
				return;
			}
			if(qidui())
			{
				for(int i=0;i<=33;i++)
				{
					if(cnt[i]==1)
					{
						System.out.println("���ƣ�");
						System.out.println("����Ϊ��"+idx[i]);
						break;
					}
				}
				return;
			}
			System.out.println("δ���ƣ�");
			return;
		}
		else//����������5������ʱ�����ܺ�ȸͷ��
		{
			//for(int i=0;i<=33;i++)System.out.println(i+" "+tingpai[i]);
			System.out.println("���ƣ�");
			System.out.print("����Ϊ:");
			for(int i=0;i<=33;i++) 
			{
				if(tingpai[i]) 
				{
					System.out.print(idx[i]+" ");
				}
			}
		}
	}
	public static void calc_tingpai()//�������ƣ����������ƣ�����������ö���ƣ��������ƣ����ܷ���������ͣ�
	{
		/*char[] ch=s.toCharArray();
		int n=ch.length;
		int[] num=new int[n];
		for(int i=0;i<=n-1;i++)num[i]=ch[i]-'0';//trans to num array*/
		for(int i=0;i<=33;i++)
		{
			jiang=false;//ÿһ�����¼���ʱ��Ҫ��ȸͷ״̬���
			int[] t_cnt=new int[34];
			for(int j=0;j<=33;j++)t_cnt[j]=cnt[j];
			t_cnt[i]+=1;//���γ���ÿһ���Ƽ��Ϻ��Ƿ��Ϊ����
			if(t_cnt[i]>4)continue;//��ֹ6666m123456p123s�����������������
			int[] aWan=new int[9];
			int[] aTong=new int[9];
			int[] aTiao=new int[9];
			int[] aZi=new int[7];
			for(int k=0;k<=8;k++)aWan[k]=t_cnt[k];
			for(int k=0;k<=8;k++)aTong[k]=t_cnt[9+k];
			for(int k=0;k<=8;k++)aTiao[k]=t_cnt[18+k];
			for(int k=0;k<=6;k++)aZi[k]=t_cnt[27+k];
			if(hu(aWan,aTong,aTiao,aZi))tingpai[i]=true;//5���ƣ�Ϊtrue
			//if(i==30)for(int k=0;k<=33;k++)System.out.print(t_cnt[k]+" ");
		}
	}
	public static int huase(int[] hua)//����������������14�ţ������ܷ�����//�ʼ��
	{
		if(hua.length==7)
		{
			for(int i=0;i<hua.length;i++)
			{
				if(hua[i]==3)
				{
					hua[i]=0;
					huase(hua);
				}
				//��������������϶��ǽ�
				if(hua[i]==2&&!jiang)
				{
					hua[i]=0;
					jiang=true;
					huase(hua);
				}
			}
		}
		else
		{
			for(int i=0;i<hua.length;i++)
			{
				//���û�н����Ȱѽ�����ȥ
				if(hua[i]>=2&&!jiang)
				{
					hua[i]-=2;
					jiang=true;
					int fanhui=huase(hua);
					//����ݹ��������û�м��꣬��ѽ��ӻ�ȥ
					if(fanhui!=0)
					{
						hua[i]+=2;
						jiang=false;
					}
				}
				if(hua[i]!=0&&i<7&&hua[i+1]!=0&&hua[i+2]!=0)
				{
					hua[i]--;
					hua[i+1]--;
					hua[i+2]--;
					huase(hua);
					int fanhui=huase(hua);
					//����ݹ��������û�м��꣬��ȥ�ļӻ�ȥ
					if(fanhui!=0)
					{
						hua[i]++;
						hua[i+1]++;
						hua[i+2]++;
					}
				}
				if(hua[i]>=3)
				{
					hua[i]-=3;
					huase(hua);
					int fanhui=huase(hua);
					//����ݹ��������û�м��꣬��ȥ�ļӻ�ȥ
					if(fanhui!=0)
					{
						hua[i]+=3;
					}
				}
			}
		}
		int re=0;
		for(int i=0;i<hua.length;i++)
		{
			re+=hua[i];
		}
		return re;
	}
	public static boolean hu(int[] aWan,int[] aTong,int[] aTiao,int[] aZi)
	{
		if(huase(aWan)==0&&huase(aTong)==0&&huase(aTiao)==0&&huase(aZi)==0&&jiang)return true;
		else return false;
	}
	public static boolean qidui()//�����ж��߶�(�������߶�)
	{
		int cnt2=0;
		int cnt1=0;
		for(int num:cnt)
		{
			if(num==2)cnt2++;
			else if(num==1)cnt1++;
		}
		return cnt2==6&&cnt1==1;
	}
	public static boolean guoshi13()//�����ж���ʿ13��
	{
		//13���۾���
		yaojiu[0]=cnt[0];
		yaojiu[1]=cnt[8];
		yaojiu[2]=cnt[9];
		yaojiu[3]=cnt[17];
		yaojiu[4]=cnt[18];
		yaojiu[5]=cnt[26];
		yaojiu[6]=cnt[27];
		yaojiu[7]=cnt[28];
		yaojiu[8]=cnt[29];
		yaojiu[9]=cnt[30];
		yaojiu[10]=cnt[31];
		yaojiu[11]=cnt[32];
		yaojiu[12]=cnt[33];
		
		int count=0;
		for(int num:yaojiu)if(num==1)count++;
		return count==13;
	}
	public static boolean guoshi()//�����ж���ʿ
	{
		//13���۾���
		yaojiu[0]=cnt[0];
		yaojiu[1]=cnt[8];
		yaojiu[2]=cnt[9];
		yaojiu[3]=cnt[17];
		yaojiu[4]=cnt[18];
		yaojiu[5]=cnt[26];
		yaojiu[6]=cnt[27];
		yaojiu[7]=cnt[28];
		yaojiu[8]=cnt[29];
		yaojiu[9]=cnt[30];
		yaojiu[10]=cnt[31];
		yaojiu[11]=cnt[32];
		yaojiu[12]=cnt[33];
		
		int count=0;
		for(int num:yaojiu)if(num>0)count++;
		return count==12;
		
	}
}
