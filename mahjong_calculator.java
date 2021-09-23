/*
 * @author:demondaydream
 * 
 * 名称：麻将听牌计算器
 * 
 * 作用：输入一个13张牌型（m、p、s、z顺序），给出听牌情况
 * 1-9m，1-9p，1-9s（0按5计算）
 * 1-7z：东南西北白发中
 * 
 * 最后再考虑七对和国士
 */
import java.util.*;
public class mahjong_calculator 
{
	public static int[] cnt=new int[34];     //分别存储34种牌的张数情况
	
	public static int[] yaojiu=new int[13];
	
	public static String[] idx= {"1m","2m","3m","4m","5m","6m","7m","8m","9m",
			                     "1p","2p","3p","4p","5p","6p","7p","8p","9p",
			                     "1s","2s","3s","4s","5s","6s","7s","8s","9s",
			                     "1z","2z","3z","4z","5z","6z","7z"};
	
	public static boolean[] tingpai=new boolean[34];//是否为待牌，与idx对应显示
	
	public static boolean jiang=false;//是否已经出现雀头计数过
	
	public static int[] yupai=new int[4];//各种牌取出完整面子后的余牌数
	
	public static void main(String[] args) 
	{
		Scanner sc=new Scanner(System.in);
		System.out.println("请输入牌型：");
		String s=sc.next();
		//System.out.println(s);     //测试牌型读入
		String[] box=new String[4];//分别存储m、p、s、z情况
		for(int i=0;i<=3;i++)
		{
			box[i]=new String("");
		}
		
		int index=-1;             //位置变量
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
		
		//每种牌张数储存进cnt
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
		
		//若总张数不是13张 则有误
		int sum=0;
		for(int num:cnt)sum+=num;
		if(sum!=13)
		{
			System.out.println("输入有误！请输入13张牌！");
			return;
		}
		
		//int suc=0;//计数成型的搭子（最多一个，否则（如果也不是七对国士）则为未听牌，第一组对子不算）
		/*for(int i=0;i<=3;i++)
		{
			String str=s;
			if(str.length()==0)continue;
			suc=calc_tingpai();//依据之前的cnt数组计数搭子，同时枚举判断听牌，更新tingpai数组
		}*/
		calc_tingpai();
		boolean flag=false;
		for(boolean ting:tingpai)if(ting) {flag=true;break;}
		if(!flag)//无法形成常规5个面子（含一雀头）时，考虑是否是非常规牌型
		{
			if(guoshi13())
			{
				System.out.println("听牌！");
				System.out.println("待牌为：1m 9m 1p 9p 1s 9s 1z 2z 3z 4z 5z 6z 7z");
				return;
			}
			if(guoshi())
			{
				System.out.println("听牌！");
				System.out.print("待牌为：");
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
						System.out.println("听牌！");
						System.out.println("待牌为："+idx[i]);
						break;
					}
				}
				return;
			}
			System.out.println("未听牌！");
			return;
		}
		else//至少能数出5个面子时（可能含雀头）
		{
			//for(int i=0;i<=33;i++)System.out.println(i+" "+tingpai[i]);
			System.out.println("听牌！");
			System.out.print("待牌为:");
			for(int i=0;i<=33;i++) 
			{
				if(tingpai[i]) 
				{
					System.out.print(idx[i]+" ");
				}
			}
		}
	}
	public static void calc_tingpai()//计算听牌（即把所有牌（除了已有四枚的牌）加上手牌，看能否组成听牌型）
	{
		/*char[] ch=s.toCharArray();
		int n=ch.length;
		int[] num=new int[n];
		for(int i=0;i<=n-1;i++)num[i]=ch[i]-'0';//trans to num array*/
		for(int i=0;i<=33;i++)
		{
			jiang=false;//每一次重新计算时，要将雀头状态清空
			int[] t_cnt=new int[34];
			for(int j=0;j<=33;j++)t_cnt[j]=cnt[j];
			t_cnt[i]+=1;//依次尝试每一种牌加上后是否成为和牌
			if(t_cnt[i]>4)continue;//防止6666m123456p123s这种情况被当做听牌
			int[] aWan=new int[9];
			int[] aTong=new int[9];
			int[] aTiao=new int[9];
			int[] aZi=new int[7];
			for(int k=0;k<=8;k++)aWan[k]=t_cnt[k];
			for(int k=0;k<=8;k++)aTong[k]=t_cnt[9+k];
			for(int k=0;k<=8;k++)aTiao[k]=t_cnt[18+k];
			for(int k=0;k<=6;k++)aZi[k]=t_cnt[27+k];
			if(hu(aWan,aTong,aTiao,aZi))tingpai[i]=true;//5搭牌，为true
			//if(i==30)for(int k=0;k<=33;k++)System.out.print(t_cnt[k]+" ");
		}
	}
	public static int huase(int[] hua)//给定各种牌张数（14张），看能否听牌//最开始的
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
				//如果字有两个，肯定是将
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
				//如果没有将，先把将减出去
				if(hua[i]>=2&&!jiang)
				{
					hua[i]-=2;
					jiang=true;
					int fanhui=huase(hua);
					//如果递归回来依旧没有减完，则把将加回去
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
					//如果递归回来依旧没有减完，减去的加回去
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
					//如果递归回来依旧没有减完，减去的加回去
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
	public static boolean qidui()//单独判定七对(不含龙七对)
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
	public static boolean guoshi13()//单独判定国士13面
	{
		//13种幺九牌
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
	public static boolean guoshi()//单独判定国士
	{
		//13种幺九牌
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
