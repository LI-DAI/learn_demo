package com.learn.admin.test;

public class Sort {

    /**
     * 冒泡排序
     * 1) 比较相邻的元素。如果第一个比第二个大，就交换它们两个。
     * 2) 对每一对相邻的元素都进行比较，等所有的比较完后最后一个数字是这堆数据里的最大数字。
     * 3) 重复步骤一，直到排序完成。
     * @param arr
     */
    public static void buildSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }

            }
        }
    }
}
