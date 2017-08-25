import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class board {
	public HashSet <String> state=new HashSet<>();
	public List<char[][]> pool=new ArrayList<>();
	public List<char[][]> tmppool=new ArrayList<>();
	public boolean find = false;
	public PriorityQueue<node> pq =new PriorityQueue<>();
	public Set<Long> explored=new HashSet<>(); 
	public int IDDFScounter=1;
	public int DP=1;
	public int ind=0;
	public void ini(char [][] board){
		for(int i=0;i<board.length;i++){
			for(int j=0;j<board[0].length;j++)
				board[i][j]='0';
		}
	}
	public void bprint(List<car> table,char [][] board){
		//System.out.println("====new state====");
		for(car c:table){
			if(c.Ori.equals("H"))
				for(int i=0;i<c.len;i++)
					board[c.row][c.col+i]=c.id;
			else
				for(int i=0;i<c.len;i++)
					board[c.row+i][c.col]=c.id;
		}
		System.out.println('\n');
		for(int i=0;i<board.length;i++)
			System.out.println(Arrays.toString(board[i]));
	}
	public void findAnswerWithIDDFS(List<car> table, int depth){
		char [][]board=new char[6][6];
		if(depth>0){
			ini(board);
			bprint(table,board);
			if(board[2][5]=='X') {find=true; tmppool.add(board); if(pool.size()==0)pool.addAll(tmppool);return;}
			for(int i=0;i<table.size();i++){
				if(table.get(i).advOk(board)){
					table.get(i).move("+"); tmppool.add(board); IDDFScounter++;
					findAnswerWithIDDFS(table,depth-1);
					table.get(i).move("-"); tmppool.remove(tmppool.size()-1);
				}
				if(table.get(i).whtOk(board)){ 
					table.get(i).move("-"); tmppool.add(board); IDDFScounter++;
					findAnswerWithIDDFS(table,depth-1);
					table.get(i).move("+"); tmppool.remove(tmppool.size()-1);
				}
			}
		}
		return;
	}
	public void IDDFS(List<car> table){
		for (int i=1;i<Integer.MAX_VALUE;i++){
			if(find) break;
			findAnswerWithIDDFS(table,i);
		}
		System.out.println("The Result:"); int s=1; 
		for(char [][] tmp :pool){
			System.out.println("Step"+String.valueOf(s));
			for(int i=0;i<tmp.length;i++)
				System.out.println(Arrays.toString(tmp[i]));
			s++;
		}
	}
	
	
	
	
	
	public long bitlize(char [][] b){
		long res=0;
		for(int i=0;i<b.length;i++){
			for(int j=0;j<b[0].length;j++){
				if(b[i][j]!='0')
					res+=(long)Math.pow(2, i*6+j);
			}
		}
		return res;
	}
	public void removeLast(PriorityQueue<node> pq){
		PriorityQueue<node> tmp=new PriorityQueue<>();
		for(int i=0;i<pq.size()-1;i++)
			tmp.offer(pq.poll());
		pq=tmp;
	}
	public void listcopy(List<car> dest,List<car> src){
		for(car c: src)
			dest.add(new car(c.id,c.row,c.col,c.len,c.Ori));
	}
	public int SMAestmate(char[][]board){
		int g=0; int h=0; int k=board[2].length-1;
		while(board[2][k]!='X'){
			h++;
			k--;
		}
		for(int i=k+1;i<board[2].length;i++){
			if(board[2][i]!='0')
				g++;
		}
		return h+g;
	}
	public List<car> findAnswerWithSMA(List<car> N, int Bound){
		int minRES=Integer.MAX_VALUE;
		List<car> res=new ArrayList<>(N);
		char [][]board=new char[6][6]; DP++; //System.out.println(DP);
		for(car c:N){
			ini(board); bprint(N,board);
			if(c.advOk(board)){
				ini(board);c.move("+");bprint(N,board);
				if(!explored.contains(bitlize(board))){
					explored.add(bitlize(board));
					if(SMAestmate(board)<minRES){
						res.clear();listcopy(res, N);minRES=SMAestmate(board);}
					if(pq.size()==Bound) removeLast(pq);
					pq.offer(new node(N,SMAestmate(board),DP));
				}
				c.move("-");
			}
			ini(board); bprint(N,board);
			if(c.whtOk(board)){
				ini(board); c.move("-"); bprint(N,board); 
				if(!explored.contains(bitlize(board))){
					explored.add(bitlize(board));
					if(SMAestmate(board)<minRES){
						res.clear();listcopy(res, N);minRES=SMAestmate(board);}
					if(pq.size()==Bound) removeLast(pq);
					pq.offer(new node(N,SMAestmate(board),DP));
				}
				c.move("+");
			}
		}
		//System.out.println(minRES); System.out.println(pq.peek().ES); 
		if(!pq.isEmpty() && minRES>pq.peek().ES){
			DP=pq.peek().depth;return pq.poll().state;}
		//Hers++;
		return res;
	}
	public void SMAstar(List<car> table){
		// BFS 
		// do A* first! 
		char [][]board=new char[6][6]; //int old=Hers;
		ini(board);
		bprint(table,board);
		explored.add(bitlize(board));
		System.out.println("Step"+ String.valueOf(ind));
		for(int i=0;i<board.length;i++)
			System.out.println(Arrays.toString(board[i]));
		while(board[2][5]!='X'){
			table=findAnswerWithSMA(table,100);
			//if(Hers<=old){
			  //old =Hers; 
			  ini(board);
			  bprint(table,board); ind++;
			  System.out.println("Step"+ String.valueOf(ind));
			  for(int i=0;i<board.length;i++)
				  System.out.println(Arrays.toString(board[i]));
			//}
		}
	}
	public int computeeffb(int N, int n){
		//System.out.println(n);
		for(int i=2;i<N-1;i++){
			int tmp=N;
			for(int j=0;j<=n;j++){
				tmp=-1;
				tmp/=i;
			}
			if(tmp==0) return i;
		}
		return 0;
	}
	public static void main(String[] args){
		List<car> board=new ArrayList<>();
		//easy 1
		
		/*board.add(new car('T',0,2,2,"V"));
		board.add(new car('X',2,2,2,"H"));
		board.add(new car('Z',1,3,3,"H"));
		board.add(new car('I',2,4,2,"V"));
		board.add(new car('B',4,4,2,"V"));*/
		
		//easy 5
		
		board.add(new car('T',0,3,2,"V"));
		board.add(new car('X',2,3,2,"H"));
		board.add(new car('Z',3,3,2,"V"));
		board.add(new car('I',0,5,3,"V"));
		board.add(new car('B',4,4,2,"H"));
		//easy 9
		
		/*board.add(new car('T',3,0,3,"H"));
		board.add(new car('X',2,3,2,"H"));
		board.add(new car('Z',2,5,3,"V"));
		board.add(new car('I',4,2,2,"V"));
		board.add(new car('B',5,3,3,"H"));
		board.add(new car('P',3,4,2,"V"));
		//board.add(new car('O',1,3,3,"H"));*/
		
		//medium 11
		/*board.add(new car('T',0,1,3,"H"));
		board.add(new car('X',2,0,2,"H"));
		board.add(new car('Z',2,2,2,"V"));
		board.add(new car('I',4,0,3,"H"));
		board.add(new car('B',0,4,2,"H"));
		board.add(new car('P',1,4,2,"H"));
		board.add(new car('O',2,5,2,"V"));
		board.add(new car('K',4,5,2,"V"));*/
		board test=new board();
		test.IDDFS(board); System.out.println(test.IDDFScounter);
		//test.SMAstar(board);
		//System.out.println(test.explored.size());
		//System.out.println(test.DP);
		//System.out.println(test.computeeffb(test.explored.size(),test.DP));
	}
}
