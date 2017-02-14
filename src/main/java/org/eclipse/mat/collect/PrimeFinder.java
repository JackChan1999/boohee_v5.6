package org.eclipse.mat.collect;

class PrimeFinder {
    PrimeFinder() {
    }

    public static int findNextPrime(int floor) {
        boolean isPrime = false;
        while (!isPrime) {
            floor++;
            isPrime = true;
            int sqrt = (int) Math.sqrt((double) floor);
            for (int i = 2; i <= sqrt; i++) {
                if ((floor / i) * i == floor) {
                    isPrime = false;
                }
            }
        }
        return floor;
    }

    public static int findPrevPrime(int ceil) {
        boolean isPrime = false;
        while (!isPrime) {
            ceil--;
            isPrime = true;
            int sqrt = (int) Math.sqrt((double) ceil);
            for (int i = 2; i <= sqrt; i++) {
                if ((ceil / i) * i == ceil) {
                    isPrime = false;
                }
            }
        }
        return ceil;
    }
}
