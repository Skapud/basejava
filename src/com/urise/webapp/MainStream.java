package com.urise.webapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainStream {
    public static void main(String[] args) {
        int[] randomDigits = new Random().ints(8, 1, 10).toArray();

        List<Integer> randomDigitsList = new ArrayList<>();
        for (int digit : randomDigits) {
            randomDigitsList.add(digit);
        }

        System.out.println("Исходные случайные значения: ");
        for (int digit : randomDigits) {
            System.out.print(digit);
        }
        System.out.println("\nМинимально возможное число из уникальных чисел: ");
        System.out.println(minValue(randomDigits));

        System.out.println("Удаление чётных/нечётных, в зависимости от суммы: ");
        for (Integer integer : oddOrEven(randomDigitsList)) {
            System.out.print(integer);
        }
    }

    public static int minValue(int[] values) {
        return IntStream.of(values)
                .distinct()
                .sorted()
                .reduce(0, (acc, x) -> acc * 10 + x);
    }

    public static List<Integer> oddOrEven(List<Integer> integers) {
        return integers.stream()
                .collect(Collectors.partitioningBy(n -> n % 2 == 0))
                .get(integers.stream().reduce(0, Integer::sum) % 2 != 0);
    }
}

