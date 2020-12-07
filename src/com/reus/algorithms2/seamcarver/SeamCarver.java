package com.reus.algorithms2.seamcarver;

import edu.princeton.cs.algs4.Picture;

import java.util.Arrays;

/**
 * https://coursera.cs.princeton.edu/algs4/assignments/seam/specification.php
 *
 * @author Konstantin Reus
 */
public class SeamCarver {
    private double[][] energy;
    private int[][] rgb;
    private double[] distTo;
    private int[] edgeTo;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        fillRgb(picture);
        calculateEnergy();
        buildGraph();
    }

    public static void main(String[] args) {
        SeamCarver s = new SeamCarver(new Picture("/Users/kreus/Downloads/seam/1x8.png"));
        s.removeVerticalSeam(new int[]{9});

    }

    private void fillRgb(Picture picture) {
        rgb = new int[picture.height()][picture.width()];
        for (int y = 0; y < picture.height(); y++) {
            for (int x = 0; x < picture.width(); x++) {
                rgb[y][x] = picture.getRGB(x, y);
            }
        }
    }

    private void buildGraph() {
        distTo = new double[rgb[0].length * rgb.length];
        edgeTo = new int[rgb[0].length * rgb.length];
        Arrays.fill(distTo, Double.POSITIVE_INFINITY);
        for (int i = 0; i < width(); i++) {
            distTo[i] = 1000;
        }
        Arrays.fill(edgeTo, -1);
        for (int y = 0; y < energy.length; y++) {
            for (int x = 0; x < energy[y].length; x++) {
                int nodeNum = getNodeNum(x, y);
                int leftChild = getNodeNum(x - 1, y + 1);
                int straightChild = getNodeNum(x, y + 1);
                int rightChild = getNodeNum(x + 1, y + 1);
                relax(nodeNum, leftChild);
                relax(nodeNum, straightChild);
                relax(nodeNum, rightChild);
            }
        }
    }

    private void calculateEnergy() {
        energy = new double[height()][width()];
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                energy[y][x] = getEnergyByCoordinates(y, x);
            }
        }
    }

    // current picture
    public Picture picture() {
        Picture p = new Picture(width(), height());
        if (width() == 0 || height() == 0) {
            return p;
        }
        for (int y = 0; y < rgb.length; y++) {
            for (int x = 0; x < rgb[y].length; x++) {
                p.setRGB(x, y, rgb[y][x]);
            }
        }
        return p;
    }

    // width of current picture
    public int width() {
        if (rgb.length == 0) {
            return 0;
        }
        return rgb[0].length;
    }

    // height of current picture
    public int height() {
        return rgb.length;
    }

    private double getEnergyByCoordinates(int y, int x) {
        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) {
            return 1000;
        }
        int rgbRight = rgb[y][x + 1];
        int rgbLeft = rgb[y][x - 1];
        double redDiffX = ((rgbRight >> 16) & 0xFF) - ((rgbLeft >> 16) & 0xFF);
        double greenDiffX = ((rgbRight >> 8) & 0xFF) - ((rgbLeft >> 8) & 0xFF);
        double blueDiffX = ((rgbRight >> 0) & 0xFF) - ((rgbLeft >> 0) & 0xFF);
        double xDegree = redDiffX * redDiffX + greenDiffX * greenDiffX + blueDiffX * blueDiffX;

        int rgbDown = rgb[y + 1][x];
        int rgbUp = rgb[y - 1][x];
        double redDiffY = ((rgbDown >> 16) & 0xFF) - ((rgbUp >> 16) & 0xFF);
        double greenDiffY = ((rgbDown >> 8) & 0xFF) - ((rgbUp >> 8) & 0xFF);
        double blueDiffY = ((rgbDown >> 0) & 0xFF) - ((rgbUp >> 0) & 0xFF);
        double yDegree = redDiffY * redDiffY + greenDiffY * greenDiffY + blueDiffY * blueDiffY;

        double degreeSum = xDegree + yDegree;

        return Math.sqrt(degreeSum);
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (rgb.length == 0 || rgb[0].length == 0 || energy.length == 0 || energy[0].length == 0) {
            return 0;
        }
        if (x < 0 || y < 0 || x >= width() || y >= height()) {
            throw new IllegalArgumentException();
        }
        return energy[y][x];
    }

    private double energyByNode(int nodeNum) {
        return energy(getXofNode(nodeNum), getYofNode(nodeNum));
    }

    private double[][] transpose(double[][] matrix) {
        double[][] result = new double[matrix[0].length][matrix.length];
        for (int y = 0; y < result.length; y++) {
            for (int x = 0; x < result[y].length; x++) {
                result[y][x] = matrix[x][y];
            }
        }
        return result;
    }

    private int[][] transpose(int[][] matrix) {
        int[][] result = new int[matrix[0].length][matrix.length];
        for (int y = 0; y < result.length; y++) {
            for (int x = 0; x < result[y].length; x++) {
                result[y][x] = matrix[x][y];
            }
        }
        return result;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        energy = transpose(energy);
        rgb = transpose(rgb);
        calculateEnergy();
        buildGraph();
        int[] result = findVerticalSeam();
        energy = transpose(energy);
        rgb = transpose(rgb);
        calculateEnergy();
        buildGraph();
        return result;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double min = Double.POSITIVE_INFINITY;
        int minIndex = -1;
        for (int x = 0; x < energy[height() - 1].length; x++) {
            if (distTo[getNodeNum(x, height() - 1)] < min) {
                min = distTo[getNodeNum(x, height() - 1)];
                minIndex = x;
            }
        }
        if (minIndex == -1) {
            return new int[]{};
        }
        return buildPathToNode(getNodeNum(minIndex, height() - 1));
    }

    private int[] buildPathToNode(int nodeNum) {
        int[] result = new int[height()];
        for (int i = height() - 1; i >= 0; i--) {
            result[i] = getXofNode(nodeNum);
            nodeNum = edgeTo[nodeNum];
        }
        return result;
    }

    private void relax(int from, int to) {
        if (from == -1 || to == -1) {
            return;
        }
        if (distTo[to] == Double.POSITIVE_INFINITY || distTo[to] > distTo[from] + energyByNode(to)) {
            distTo[to] = distTo[from] + energyByNode(to);
            edgeTo[to] = from;
        }
    }

    private int getNodeNum(int x, int y) {
        if (x < 0 || y < 0 || x >= width() || y >= height()) {
            return -1;
        }
        return y * width() + x;
    }

    private int getXofNode(int nodeNum) {
        return nodeNum % width();
    }

    private int getYofNode(int nodeNum) {
        return nodeNum / width();
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (seam.length == 0 || width() == 0) {
            return;
        }
        energy = transpose(energy);
        rgb = transpose(rgb);
        calculateEnergy();
        buildGraph();
        removeVerticalSeam(seam);
        energy = transpose(energy);
        rgb = transpose(rgb);
        calculateEnergy();
        buildGraph();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (seam.length == 0 || height() == 0) {
            return;
        }
        validateSeam(seam);
        int[][] newRgb = new int[rgb.length][rgb[0].length - 1];
        double[][] newEnergy = new double[energy.length][energy[0].length - 1];

        for (int y = 0; y < seam.length; y++) {
            int xToRemove = seam[y];
            System.arraycopy(rgb[y], 0, newRgb[y], 0, xToRemove);
            System.arraycopy(energy[y], 0, newEnergy[y], 0, xToRemove);

            System.arraycopy(rgb[y], xToRemove + 1, newRgb[y], xToRemove, newRgb[y].length - xToRemove);
            System.arraycopy(energy[y], xToRemove + 1, newEnergy[y], xToRemove, newEnergy[y].length - xToRemove);
        }
        rgb = newRgb;
        energy = newEnergy;
        calculateEnergy();
        buildGraph();
    }

    private void validateSeam(int[] seam) {
        if (seam == null || seam.length != height() || width() <= 1) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= width()) {
                throw new IllegalArgumentException();
            }
        }
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
    }

}
