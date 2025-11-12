import java.util.*;
import java.time.*;

public class Main{
    static class Order{
        int id;
        String name;
        LocalDateTime ts;
        
        Order(int id , String name , LocalDateTime timestamp){
             this.id = id;
            this.name = name;
            this.ts = timestamp;
            
        }
        
        public String toString(){
            return id+" |  "+name+" |  "+ts;
        }
    }
    
    static List<Order> generateOrders(int count){
        List<Order> orders = new ArrayList<>();
        Random rand = new Random();
        
        for(int i = 1;i<=count;i++){
            String name = "Customer_" + rand.nextInt(count);
            LocalDateTime ts = LocalDateTime.now().minusSeconds(rand.nextInt(count));
            
            orders.add(new Order(i,name,ts));
        }
        return orders;
    }
    
    static void mergeSort(List<Order> orders , int left , int right){
        if(left < right){
            int mid = (left+right)/2;
            mergeSort(orders, left,mid);
            mergeSort(orders, mid+1, right);
            merge(orders, left , mid , right);
        }
    }
    
    static void merge(List<Order> orders, int l, int m , int r){
        List<Order> L = new ArrayList<>(orders.subList(l,m+1));
        List<Order> R = new ArrayList<>(orders.subList(m+1, r+1));
        int i = 0 , j = 0, k = l;
        while(i < L.size() && j < R.size()){
        if(L.get(i).ts.isBefore(R.get(j).ts)){
            orders.set(k++ , L.get(i++));
        }
        else{
            orders.set(k++ , R.get(j++));
        }
        }
        
        while(i < L.size()) orders.set(k++ , L.get(i++));
        while(j < R.size()) orders.set(k++ , R.get(j++));
        
        
    }
    
    public static void main(String[] args){
        List<Order> order_list = new ArrayList<>();
        int count = 1_000_000;
        order_list = generateOrders(count);
        
        long start = System.currentTimeMillis();
        mergeSort(order_list, 0 , order_list.size() -1 );
        long end = System.currentTimeMillis();
        
        System.out.println("Time required for Sorting - "+(end - start)+" ms");
        System.out.println("First 10 orders - ");
        
        for(int i = 1;i<=10;i++){
            System.out.println(order_list.get(i));
        }
        
        
        
    }
    
    
}


