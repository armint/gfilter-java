package org.firepick.gfilter;

import org.junit.Assert;
import org.junit.Test;

public class Matrix3x3Test {

	@Test
	public void testMatrix3x3() {
	    Matrix3x3 mat = new Matrix3x3( 
	    		1, 2, 3,
	            0, 1, 4,
	            5, 6, 0);
	    Matrix3x3 matCopy = new Matrix3x3( 
	    		1, 2, 3,
	            0, 1, 4,
	            5, 6, 0);
		Assert.assertEquals(mat, matCopy);
		Matrix3x3 matInv = mat.inverse();
		Assert.assertEquals(new Matrix3x3(-24, 18, 5, 20, -15, -4, -5, 4, 1), matInv);
	}

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
