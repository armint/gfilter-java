package org.firepick.gfilter;

import java.io.Serializable;
import java.util.Arrays;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Matrix3x3 implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int MSCALE_X = 0;
	public static final int MSKEW_X = 1;
	public static final int MTRANS_X = 2;
	public static final int MSKEW_Y = 3;
	public static final int MSCALE_Y = 4;
	public static final int MTRANS_Y = 5;

	private double[] elements;

	public Matrix3x3() {
		elements = new double[9];
		// identity matrix
		elements[0] = 1;
		elements[4] = 1;
		elements[8] = 1;
	}

	public Matrix3x3(Matrix3x3 matrix) {
		elements = Arrays.copyOf(matrix.elements, 9);
	}

	protected double[] getElements() {
		return elements;
	}

	public Matrix3x3(double... elements) {
		this.elements = Arrays.copyOf(elements, 9);
		if (elements.length < 9) {
			this.elements[8] = 1;
		}
	}

	public void getValues(double[] values) {
		for (int i = 0; i < values.length; i++) {
			values[i] = elements[i];
		}
	}

	public double getValue(int index) {
		return elements[index];
	}

	public double at(int y, int x) {
		return elements[y * 3 + x];
	}

	public void setValues(double[] values) {
		for (int i = 0; i < values.length; i++) {
			elements[i] = values[i];
		}
	}

	@Override
	public String toString() {
		return "Matrix [elements=" + Arrays.toString(elements) + "]";
	}

	public Matrix3x3 inverse() {
		double[] a = Arrays.copyOf(elements, 9);;
		/*
			| a11 a12 a13 |-1             |   a33a22-a32a23  -(a33a12-a32a13)   a23a12-a22a13  |
			| a21 a22 a23 |    =  1/DET * | -(a33a21-a31a23)   a33a11-a31a13  -(a23a11-a21a13) |
			| a31 a32 a33 |               |   a32a21-a31a22  -(a32a11-a31a12)   a22a11-a21a12  |
			
			with DET  =  a11(a33a22-a32a23)-a21(a33a12-a32a13)+a31(a23a12-a22a13)
		 */

		double determinant = determinant();
		if (Math.abs(determinant) < 1e-10) {
			return null;
		}

		elements[0] = (a[8] * a[4] - a[7] * a[5]) / determinant;
		elements[1] = -(a[8] * a[1] - a[7] * a[2]) / determinant;
		elements[2] = (a[5] * a[1] - a[4] * a[2]) / determinant;
		elements[3] = -(a[8] * a[3] - a[6] * a[5]) / determinant;
		elements[4] = (a[8] * a[0] - a[6] * a[2]) / determinant;
		elements[5] = -(a[5] * a[0] - a[3] * a[2]) / determinant;
		elements[6] = (a[7] * a[3] - a[6] * a[4]) / determinant;
		elements[7] = -(a[7] * a[0] - a[6] * a[1]) / determinant;
		elements[8] = (a[4] * a[0] - a[3] * a[1]) / determinant;
		return this;

	}

	public double determinant() {
		double[] a = elements;
		double determinant = a[0] * (a[8] * a[4] - a[7] * a[5]) - a[3] * (a[8] * a[1] - a[7] * a[2]) + a[6] * (a[5] * a[1] - a[4] * a[2]);
		return determinant;
	}

}
