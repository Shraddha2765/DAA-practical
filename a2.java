import java.io.*;
import java.util.*;

public class QuickSortMovies {

    static class Movie {
        int id;
        String title;
        double rating;
        int year;

        Movie(int id, String title, double rating, int year) {
            this.id = id;
            this.title = title;
            this.rating = rating;
            this.year = year;
        }

        public String toString() {
            return id + "," + title + "," + rating + "," + year;
        }
    }

    // ---------- QUICKSORT ----------
    static void quickSort(List<Movie> list, int low, int high, String sortBy) {
        if (low < high) {
            int pi = partition(list, low, high, sortBy);
            quickSort(list, low, pi - 1, sortBy);
            quickSort(list, pi + 1, high, sortBy);
        }
    }

    static int partition(List<Movie> list, int low, int high, String sortBy) {
        Movie pivot = list.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            boolean condition = false;
            switch (sortBy) {
                case "rating" -> condition = list.get(j).rating >= pivot.rating;
                case "year" -> condition = list.get(j).year >= pivot.year;
            }
            if (condition) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }

    // ---------- CSV GENERATION ----------
    static void generateCSV(String filename, int count) throws IOException {
        FileWriter fw = new FileWriter(filename);
        fw.write("MovieID,Title,Rating,Year\n");
        Random rand = new Random();
        for (int i = 1; i <= count; i++) {
            String title = "Movie_" + i;
            double rating = 5 + rand.nextDouble() * 5; // 5.0 - 10.0
            int year = 1980 + rand.nextInt(45);
            fw.write(i + "," + title + "," + String.format("%.2f", rating) + "," + year + "\n");
        }
        fw.close();
        System.out.println("✅ Movies CSV file created with " + count + " records!");
    }

    // ---------- CSV READING ----------
    static List<Movie> readCSV(String filename) throws Exception {
        List<Movie> list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        br.readLine(); // header
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            String title = parts[1];
            double rating = Double.parseDouble(parts[2]);
            int year = Integer.parseInt(parts[3]);
            list.add(new Movie(id, title, rating, year));
        }
        br.close();
        return list;
    }

    public static void main(String[] args) throws Exception {
        String file = "movies.csv";
        int count = 1_000_000;

        // Uncomment to generate file
        // generateCSV(file, count);

        List<Movie> movies = readCSV(file);

        // Sort by rating
        long start = System.currentTimeMillis();
        quickSort(movies, 0, movies.size() - 1, "rating");
        long end = System.currentTimeMillis();

        System.out.println("\n✅ Quick Sort by rating completed in " + (end - start) + " ms");

        // Built-in sort comparison
        List<Movie> copy = new ArrayList<>(movies);
        long t1 = System.currentTimeMillis();
        copy.sort((a, b) -> Double.compare(b.rating, a.rating));
        long t2 = System.currentTimeMillis();

        System.out.println("✅ Built-in sort completed in " + (t2 - t1) + " ms");

        System.out.println("\nTop 5 Movies by rating:");
        for (int i = 0; i < 5; i++) System.out.println(movies.get(i));
    }
}


