package org.firepick.gfilter;

import java.io.Serializable;
import java.util.Arrays;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(elements);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matrix3x3 other = (Matrix3x3) obj;
		if (!Arrays.equals(elements, other.elements))
			return false;
		return true;
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

	public double getRotationAngle() {
		double angle = Math.atan2(elements[3], elements[4]) * 180 / Math.PI;
		return angle < 0 ? angle + 360 : angle;
	}

	public double mapAngle(double angle) {
		double rads = Math.toRadians(angle);
		double cos = Math.cos(rads);
		double sin = Math.sin(rads);
		double mappedX = cos * getValue(Matrix3x3.MSCALE_X) + sin * getValue(Matrix3x3.MSKEW_X);
		double mappedY = cos * getValue(Matrix3x3.MSKEW_Y) + sin * getValue(Matrix3x3.MSCALE_Y);
		double degrees = (double) Math.toDegrees(Math.atan2(mappedY, mappedX));
		if (degrees < 0) {
			degrees += 360;
		}
		return degrees;

	}

	public double det2x2(int atY, int atX) {
		int x1 = atX == 0 ? 1 : 0;
		int x2 = atX == 2 ? 1 : 2;
		int y1 = atY == 0 ? 1 : 0;
		int y2 = atY == 2 ? 1 : 2;

		return elements[y1 * 3 + x1] * elements[y2 * 3 + x2] - elements[y1 * 3 + x2] * elements[y2 * 3 + x1];

	}

	public double det3x3() {
		return elements[0] * det2x2(0, 0) - elements[1] * det2x2(0, 1) + elements[2] * det2x2(0, 2);
	}

	public double determinant() {
		double[] a = elements;
		/*
			| a11 a12 a13 |-1             |   a33a22-a32a23  -(a33a12-a32a13)   a23a12-a22a13  |
			| a21 a22 a23 |    =  1/DET * | -(a33a21-a31a23)   a33a11-a31a13  -(a23a11-a21a13) |
			| a31 a32 a33 |               |   a32a21-a31a22  -(a32a11-a31a12)   a22a11-a21a12  |
			
			with DET  =  a11(a33a22-a32a23)-a21(a33a12-a32a13)+a31(a23a12-a22a13)
		 */

		double determinant = a[0] * (a[8] * a[4] - a[7] * a[5]) - a[3] * (a[8] * a[1] - a[7] * a[2]) + a[6] * (a[5] * a[1] - a[4] * a[2]);
		return determinant;
	}

}
