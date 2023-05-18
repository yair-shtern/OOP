package algo_questions;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * A class of solutions for the algo questions.
 *
 * @author Yair Stern
 */
public class Solutions {
    /**
     * Method computing the maximal amount of tasks out of n tasks that can be completed with m time slots.
     *
     * @param tasks     - array of integers of length n.
     *                  tasks[i] is the time in hours required to complete task i.
     * @param timeSlots - array of integers of length m. timeSlots[i] is the length in hours of the slot i.
     * @return maximal amount of tasks that can be completed
     */
    public static int alotStudyTime(int[] tasks, int[] timeSlots) {
        //Greedy Algorithm
        //sort the two arrays
        Arrays.sort(tasks);
        Arrays.sort(timeSlots);
        int maxTasks = 0;
        int j = 0;
        for (int task : tasks) {
            //for each task try to find a slot
            while (j < timeSlots.length) {
                if (task <= timeSlots[j]) {
                    maxTasks++;
                    j++;
                    break;
                }
                if (j == timeSlots.length - 1) {
                    //if we are here there is no more open slots
                    return maxTasks;
                }
                j++;
            }
        }
        return maxTasks;
    }

    /**
     * Method computing the nim amount of leaps a frog needs to jumb across n waterlily leaves,
     * from leaf 1 to leaf n. The leaves vary in size and how stable they are,
     * so some leaves allow larger leaps than others.
     * leapNum[i] is an integer telling you how many leaves ahead you can jump from leaf i.
     * If leapNum[3]=4, the frog can jump from leaf 3, and land on any of the leaves 4, 5, 6 or 7.
     *
     * @param leapNum - array of ints. leapNum[i] is how many leaves ahead you can jump from leaf i.
     * @return minimal no. of leaps to last leaf.
     */
    public static int minLeap(int[] leapNum) {
        //Greedy Algorithm
        int minLeaps = 0;
        int i = 0;
        int jmpTo = 0;
        int prevJmp = 1;
        //go through the array
        while (i < leapNum.length - 1) {
            int maxJmp = i + 1;
            if (i + leapNum[i] >= leapNum.length - 1) {
                //we reach to last leaf on the next jump
                return minLeaps + 1;
            }
            //for each leap search for the biggest jump in the leaves in the range of the jump
            for (int j = prevJmp; j <= i + leapNum[i] && j < leapNum.length; j++) {
                if (j + leapNum[j] > maxJmp) {
                    maxJmp = j + leapNum[j];
                    jmpTo = j;
                }
            }
            //jump to the most 'profitable' leaf
            prevJmp = i + leapNum[i];
            i = jmpTo;
            minLeaps++;
        }
        return minLeaps;
    }

    /**
     * Method computing the solution to the following problem: A boy is filling the water trough
     * for his father's cows in their village. The trough holds n liters of water.
     * With every trip to the village well, he can return using either the 2 bucket yoke,
     * or simply with a single bucket. A bucket holds 1 liter. In how many different ways can he fill the
     * water trough? n can be assumed to be greater or equal to 0, less than or equal to 45.
     *
     * @param n - num of liters
     * @return valid output of algorithm.
     */
    public static int bucketWalk(int n) {
        //Dynamic Programming
        int[] buckets = new int[n + 1];
        for (int i = 0; i < n + 1; i++) {
            if (i == 0 || i == 1) {
                buckets[i] = 1;
            } else {//for k buckets the options is the options for k-1 buckets + the options for k-2 buckets
                buckets[i] = buckets[i - 1] + buckets[i - 2];
            }
        }
        return buckets[n];
    }


    /**
     * Method computing the solution to the following problem: Given an integer n,
     * return the number of structurally unique BST's (binary search trees) which has exactly n nodes
     * of unique values from 1 to n. You can assume n is at least 1 and at most 19. (Definition:
     * two trees S and T are structurally distinct if one can not be obtained from the
     * other by renaming of the nodes.) (credit: LeetCode)
     *
     * @param n - num of nodes.
     * @return valid output of algorithm.
     */
    public static int numTrees(int n) {
        //Dynamic Programming
        int[] trees = IntStream.range(0, n + 1).map(i -> 0).toArray();
        for (int i = 0; i < n + 1; i++) {
            if (i == 0 || i == 1) {
                trees[i] = 1;
                continue;
            }//for 2<=k<=n nodes the options are:
            // if there are 0<=j<=k nodes in the left subtree
            // multiply the option for j by the options for the k-j (the right side)
            for (int j = 0; j < i; j++) {
                trees[i] += trees[j] * trees[i - j - 1];
            }
        }
        return trees[n];
    }
}
