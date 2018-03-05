//                            _ooOoo_    
//                           o8888888o    
//                           88" . "88    
//                           (| -_- |)    
//                            O\ = /O    
//                        ____/`---'\____    
//                      .   ' \\| |// `.    
//                       / \\||| : |||// \    
//                     / _||||| -:- |||||- \    
//                       | | \\\ - /// | |    
//                     | \_| ''\---/'' | |    
//                      \ .-\__ `-` ___/-. /    
//                   ___`. .' /--.--\ `. . __    
//                ."" '< `.___\_<|>_/___.' >'"".    
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |    
//                 \ \ `-. \_ __\ /__ _/ .-` / /    
//         ======`-.____`-.___\_____/___.-`____.-'======    
//                            `=---='    
//                God protect this program from BUG
//         .............................................    
package TCSS543_3edition;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.IntStream;

class Vertex{
	int Vertex_id;
	int [] Vertex_adjacent;
	int Vertex_color = -1;
	int saturation;
	
	
	public static String [] assign(int arge) {
		String[] arr = new String[10];
		for (int i = 0; i <= arge - 1; i++) {
			arr[i] = Integer.toString(i);
		}
		return arr;
		
	}
}



class TCSS543{
	static HashMap color_map = new HashMap();
	public static void main(String[] args) throws IOException {
		double density = 0.5;
		String str = "The density of edge is : "+density +"\r\n";
		String time;
		String color;
		int [] nums = new int []{10, 15, 20, 30, 40, 60, 100, 150, 200};
		for (int i = 0; i < nums.length; i ++) {
			
			long time_every_test = 0;
			long color_every_test = 0;
			int testtime = 100;
			for (int j = 0; j < testtime; j ++){
				long [] tem = TCSS543.Tcss(nums[i], density);
				time_every_test += tem[0];
				color_every_test += tem[1];
				color_map.clear();
			}
			float out_color = color_every_test;
			float out_time = time_every_test;
			time =  String.valueOf(out_time/testtime);
			color = String.valueOf(out_color/testtime);
			str += "test "+ i + " : " +"\r\n";
			str += "The number of vertex : " +nums[i] + "\r\n";
			str += "The time of coloring : " +time +"ms"+ "\r\n";
			str += "The number of color been used : " +color + "\r\n";
		}
		File file = new File("C:" + File.separator + "Documents and Settings" + File.separator + "Administrator" +  File.separator + "Desktop" + File.separator + "output.txt");
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        
        Writer out = new FileWriter(file);
        out.write(str);
        out.close();
	}
	public static long[] Tcss(int num, double density) {
		
		
		Vertex[] vertex = new Vertex[num];
		//create an array for Vertex class
		for(int i = 0; i < num; i++){
			vertex[i] = new Vertex();
			vertex[i].Vertex_id = i;
		}
		
		
		//assign edge
		int [][] Edge = null;
		Edge = TCSS543.create_edge(num, density);
		
		//assign adjacent	
		for(int i = 0; i < num; i++){
			vertex[i].Vertex_adjacent = new int [1];
			vertex[i].Vertex_adjacent[0] = i;
		}
		for (int i = 0; i < Edge.length; i++) {
			int element0 = Edge[i][0];
			int element1 = Edge[i][1];
			vertex[element0].Vertex_adjacent = array_operation.insertElement(vertex[element0].Vertex_adjacent, element1, vertex[element0].Vertex_adjacent.length); 
			vertex[element1].Vertex_adjacent = array_operation.insertElement(vertex[element1].Vertex_adjacent, element0, vertex[element1].Vertex_adjacent.length); 
		}
		
		
		//assign saturation
		for (int i = 0; i < num; i++) {
			vertex[i].saturation = vertex[i].Vertex_adjacent.length;
		}
		
		//set an array sorted by the saturation
		int [] sort_array = new int [0];
		sort_array = TCSS543.sort_array(sort_array, vertex, num);

		//related vertexes assign
		int [] related_vertex = new int[vertex[sort_array[0]].Vertex_adjacent.length];
		for (int i = 0; i < vertex[sort_array[0]].Vertex_adjacent.length; i++) {
			related_vertex[i] = vertex[sort_array[0]].Vertex_adjacent[i];
		}
		related_vertex = TCSS543.sort_vertex(related_vertex, sort_array);
		
		//colored vertexes assign
		int [] colored_vertex = new int[1];
		colored_vertex[0] = vertex[sort_array[0]].Vertex_id;
		
		//set a color map with the color as key and the time it used as value
		
		color_map.put(0, 1);
		
		
		//add color to vertex
		vertex[sort_array[0]].Vertex_color = 0;
		related_vertex = TCSS543.sort_vertex(related_vertex, sort_array);
		
		
		//colored_vertex (main!)
		long a=System.currentTimeMillis();
		int next_vertex;
		int next_color;
		int [] color_beenused = new int [1];
		color_beenused[0] = 0;
		
		for (int i = 0; i < num - 1; i++) {
			int tem = 0;
			next_vertex = TCSS543.next_to_color(colored_vertex, related_vertex);
			if (next_vertex == -1) {
				next_vertex = TCSS543.find_next(sort_array, colored_vertex);
				tem = -1;
			}
			next_color = TCSS543.the_color(vertex, next_vertex, colored_vertex, color_beenused);			
			if( tem != -1) {
				//if the vertex related with no one, the color should not be add to color used
				TCSS543.color_register(next_color);//color_map
			}
			vertex[next_vertex].Vertex_color = next_color;
			colored_vertex = array_operation.insertElement(colored_vertex, next_vertex, colored_vertex.length);
			related_vertex = TCSS543.related_vertex(related_vertex, vertex[next_vertex].Vertex_adjacent);
			related_vertex = TCSS543.sort_vertex(related_vertex, sort_array);
			color_beenused = array_operation.insertElement(color_beenused, next_color, color_beenused.length);
			
		}
		related_vertex = TCSS543.sort_vertex(related_vertex, sort_array);
		//System.out.println("the color been used is =" + color_beenused.length);
		long [] res= new long [] {(System.currentTimeMillis()-a), color_beenused.length};
		return  res;
	}
	public static int [][] create_edge(int num, double density)  {
		int [][] t = TCSS543.two_dimension(num);
		t = TCSS543.dense(t,density);
		return t;
	}
	
	public static int[][] dense(int[][] t, double density) {
		int [][] res = new int [0][0];
		for (int i = 0; i < t.length; i ++) {
			double possi = Math.random();
			if (density > possi) {
				int[] element = new int [2];
				element[0] = t[i][0];
				element[1] = t[i][1];
				res = test.insert_twi_array(res, element, res.length);
			}
			
		}
		return res;
	}
	public static int[][] two_dimension(int num) {
		int [] ori = new int [num];
		int [][] res = new int [0][0];
		for (int i = 0; i < num; i ++) {
			ori[i] = i;
		}
		int len =num*(num-1)/2;
		
		int t = 0;
		for (int i = 0; i < num; i ++) {
			for (int j = i+1; j < num; j ++) {
				int [] a = new int [] {i, j};
			
				res = array_operation.insert_twi_array( res, a, res.length);
			}
		}
		
		return res;
	}
///////////////////////////////////////
	public static int find_next(int[] sort_array, int[] colored_vertex) {

		for (int i = 0; i < sort_array.length; i ++) {
			int element = sort_array[i];
			if (IntStream.of(colored_vertex).anyMatch(a -> a == element) == false) {
				return element;
			}
		}
		return -1;
	}


	public static int[] sort_array(int[] sort_array, Vertex[] vertex, int num) {
		for (int i = num; i >= 0; i--) {
			for (int j = 0; j < num; j++) {
				if (vertex[j].saturation == i) {
					sort_array = array_operation.insertElement(sort_array, j, sort_array.length);				
				}
			}
		}
		return sort_array;
	}

	public static int[][] new_assign_edge(int num, int edge_num) {
		
		int [][] Edge = null;
		for ( int i = 0; i < edge_num; i ++) {
			
			int x=(int)(Math.random()*num);
			int y=(int)(Math.random()*num);
			Edge = TCSS543.edge_add(Edge, x, y);
			
		}
		
		Edge = TCSS543.del_twi_repeat(Edge);
		return Edge;
	}

	public static int the_color(Vertex[] vertex, int id, int[] colored_vertex, int[] color_beenused) {
		//set the color used by adjacent vertex
		int [] color = new int [1];
		color[0] = -1;
		int element;
		for (int i = 0; i < vertex[id].Vertex_adjacent.length; i ++) {
			element = vertex[vertex[id].Vertex_adjacent[i]].Vertex_color;
			
			if ( element >=0 ) {//the adjacent vertex color
				color = array_operation.insertElement(color, element, color.length);
			}
		}
		
		//find a unused color
		for (int i = 0; i < color_beenused.length; i ++) {
			int element_ = color_beenused[i];
			if (IntStream.of(color).anyMatch(a -> a == element_) == false) {
				return element_;
			}
		}
		
		int max = array_operation.getMax(color_beenused);
		return max + 1;
	}
	
	
	public static void color_register(int next_color) {
		if (color_map.get(next_color) == null) {
			color_map.put(next_color, 1);
		}
		else {
			int tem;
			tem = (int) color_map.get(next_color);
			color_map.put(next_color, tem + 1);
		}
	}

	




	public static int[][] del_twi_repeat(int[][] edge) {
		int [][] arr = new int [1][2];
		arr[0][0] = edge[0][0];
		arr[0][1] = edge[0][1];
		int i = 0;
		while (i < edge.length) {
			if (TCSS543.find_inarr(arr, edge[i])) {
				arr = array_operation.insert_twi_array(arr, edge[i], arr.length);
				i++;
			}
			else {
				i++;
			}
		}
		
		return arr;
	}	

	public static boolean find_inarr(int[][] arr, int[] edges) {
		for (int i = 0; i < arr.length; i++) {
			if (TCSS543.samearray(arr[i], edges)) {
				return false;
			}
		}
		return true;
		
	}

	
	
	private static boolean samearray(int[] is, int[] edges) {
		boolean c = is[0] == edges[0];
		if (c) {
			boolean c1 = is[1] == edges[1];
			if (c1) {
				return true;
			}
		}
		return false;
	}

	private static int[][] edge_add(int[][] edge, int x, int y) {
		int [] element = new int [2];
		if (x > y) {
			element[0] = y;
			element[1] = x;
		}
		else {
			element[0] = x;
			element[1] = y;
		}
		
		if (edge == null) {
			int [][] a = new int [1][2];
			a[0] = element;
			return a;
		}
		if (x == y) {
			return edge;
		}
		edge = array_operation.insert_twi_array(edge, element, edge.length);
		return edge;
	}

	private static int[] related_vertex(int[] related_vertex, int [] new_adjacent) {
		for (int i = 0; i < new_adjacent.length; i ++) {
			related_vertex = array_operation.insertElement(related_vertex, new_adjacent[i], related_vertex.length);
		}
		return related_vertex;
	}

	private static int [] color_vertex(int[] colored_vertex, int new_vertex) {
		colored_vertex = array_operation.insertElement(colored_vertex, new_vertex, colored_vertex.length);
		return colored_vertex;
	}

	public static int[] sort_vertex(int[] related_vertex, int [] sort_array) {
		int [] arr = new int [0];
		for (int i = 0 ; i < sort_array.length ; i++) {
			int element = sort_array[i];
			if (IntStream.of(related_vertex).anyMatch(a -> a == element)) {
				arr = array_operation.insertElement(arr, sort_array[i], arr.length);
			}
		}
		return arr;
	}




	public static int  next_to_color(int [] colored_vertex, int [] related_vertex) {
		for (int i = 0; i < related_vertex.length; i++) {
			int element = related_vertex[i];
			if (IntStream.of(colored_vertex).anyMatch(a -> a == element) != true) {
				colored_vertex = array_operation.insertElement(colored_vertex, element, colored_vertex.length);
				return element;
			}
		}
		return -1;
	}
}






class array_operation{
	 public static int getMax(int[] arr)  
	    {  
	        int max = arr[0];  
	        for(int i=0;i<arr.length;i++)  
	        {  
	            if(arr[i]>max)  
	                max = arr[i];  
	        }  
	        return max;  
	    }  
	 
	 public static int[][] insert_twi_array(int original[][], int[] element, int index) {
			if (index == 0) {
				int[][] a = new int [1][2];
				a[0][0] = element[0];
				a[0][1] = element[1];
				return a;
			}
			int length = original.length;
			int destination[][] = new int[length + 1][2];
			System.arraycopy(original, 0, destination, 0, index);
			destination[index][0] = element[0];
			destination[index][1] = element[1];
			System.arraycopy(original, index, destination, index + 1, length - index);
			return destination;
		   
		}
	public static int[] insertElement(int original[], int element, int index) {
		if (IntStream.of(original).anyMatch(a -> a == element)) {
			return original;
		}
		else {
	    	int length = original.length;
	    	int destination[] = new int[length + 1];
	    	System.arraycopy(original, 0, destination, 0, index);
	    	destination[index] = element;
	    	System.arraycopy(original, index, destination, index + 1, length - index);
	    	return destination;
	   }
	}
	public static int [] del_repeat(int[] arr){
		int [] list = new int[0];
        for (int i = 0; i < arr.length; i ++) {
        	list = array_operation.insertElement(list, arr[i], list.length);
        }
        return list;
	}
	public static int [] insert_array(int[] arr1, int[] arr2){
        for (int i = 0; i < arr2.length; i ++) {
        	arr1 = array_operation.insertElement(arr1, arr2[i], arr1.length);
        }
        return arr1;
	}
}
