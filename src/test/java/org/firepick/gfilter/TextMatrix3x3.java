package org.firepick.gfilter;

import org.junit.Assert;
import org.junit.Test;


public class TextMatrix3x3 {

	
	@Test
	public void testInverse() {
		Matrix3x3 matrix = new Matrix3x3(new double[] { 2, 3, 5, 7, 11, 13, 17, 19, 23 });
		Matrix3x3 inverse = matrix.inverse();
		System.out.println("Invert Matrix");
		System.out.println("Matrix: " + matrix);
		System.out.println("Inverse: " + inverse);
		Matrix3x3 expected = new Matrix3x3(new double[] { -6d / 78, -26d / 78, 16d / 78, -60d / 78, 39d / 78, -9d / 78, 54d / 78, -13d / 78, -1d / 78 });
		System.out.println("Expected: " + expected);
		System.out.println("-------------");
		Assert.assertEquals(expected, inverse);
	}
	

	@Test
	public void testDeterminant() {
		Matrix3x3 matrix = new Matrix3x3(new double[] { 2, 3, 5, 7, 11, 13, 17, 19, 23 });
		double det3x3 = matrix.det3x3();
		double determinant = matrix.determinant();
		Assert.assertEquals(det3x3, determinant, 0);
		System.out.println("Determinant: " + determinant);
	}
}
