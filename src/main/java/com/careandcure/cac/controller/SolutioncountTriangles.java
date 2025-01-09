package com.careandcure.cac.controller;

import java.util.Arrays;

class SolutioncountTriangles {
    // Function to count the number of possible triangles.
     static int countTriangles(int arr[]) {
        int n = arr.length;
        if (n < 3) return 0; // Not enough sides for a triangle

        Arrays.sort(arr); // Step 1: Sort the array
        int count = 0;

        // Step 2: Fix the largest side
        for (int k = n - 1; k >= 2; k--) {
            int i = 0, j = k - 1;

            // Step 3: Two-pointer technique
            while (i < j) {
                if (arr[i] + arr[j] > arr[k]) {
                    // All elements between i and j satisfy the condition
                    count += (j - i);
                    j--; // Move the right pointer left
                } else {
                    i++; // Move the left pointer right
                }
            }
        }
        return count; // Return the total count
    }

}