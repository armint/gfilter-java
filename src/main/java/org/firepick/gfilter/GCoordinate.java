package org.firepick.gfilter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@EqualsAndHashCode
@Getter
@Setter
public class GCoordinate implements Comparable<GCoordinate> {
	private double x;
	private double y;
	private double z;

	public GCoordinate() {
		this(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

	public GCoordinate(GCoordinate orig) {
		x = orig.x;
		y = orig.y;
		z = orig.z;
	}

	public GCoordinate(double xPos, double yPos, double zPos) {
		x = xPos;
		y = yPos;
		z = zPos;
	}

	public double distance2(GCoordinate that) {
		double dx = x - that.x;
		double dy = y - that.y;
		double dz = z - that.z;
		return dx * dx + dy * dy + dz * dz;
	}

	public String toString() {
		return "(" + x + "," + y + "," + z + ")";
	}

	public GCoordinate trunc(int places) {
		double limit = Math.pow(10.0, -places);
		if (-limit < x && x < limit) {
			x = 0;
		}
		if (-limit < y && y < limit) {
			y = 0;
		}
		if (-limit < z && z < limit) {
			z = 0;
		}
		return this;
	}

	boolean isValid() {
		return x != Double.POSITIVE_INFINITY && y != Double.POSITIVE_INFINITY && z != Double.POSITIVE_INFINITY;
	}

	@JsonIgnore
	public double getNorm2() {
		return x * x + y * y + z * z;
	}

	public GCoordinate add(GCoordinate rhs) {
		x += rhs.x;
		y += rhs.y;
		z += rhs.z;
		return this;
	}

	public GCoordinate subtract(GCoordinate rhs) {
		x -= rhs.x;
		y -= rhs.y;
		z -= rhs.z;
		return this;
	}

	public GCoordinate preMultiply(double scalar) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
		return this;
	}

	public GCoordinate preMultiply(Matrix3x3 mat) {
		x = mat.at(0, 0) * getX() + mat.at(0, 1) * getY() + mat.at(0, 2) * getZ();
		y = mat.at(1, 0) * getX() + mat.at(1, 1) * getY() + mat.at(1, 2) * getZ();
		z = mat.at(2, 0) * getX() + mat.at(2, 1) * getY() + mat.at(2, 2) * getZ();
		return this;
	}

	public int compareTo(GCoordinate o) {
		double cmp = getNorm2() - o.getNorm2();
		if (cmp == 0) {
			cmp = x - o.x;
		}
		if (cmp == 0) {
			cmp = y - o.y;
		}
		if (cmp == 0) {
			cmp = z - o.z;
		}
		return (int) Math.signum(cmp);
	}

	public GCoordinate barycentric(GCoordinate c1, GCoordinate c2, GCoordinate c3, GCoordinate c4) {
		Matrix3x3 T = new Matrix3x3(c1.x - c4.x, c2.x - c4.x, c3.x - c4.x, c1.y - c4.y, c2.y - c4.y, c3.y - c4.y, c1.z - c4.z, c2.z - c4.z, c3.z - c4.z);
		Matrix3x3 Tinv = T.inverse();
		if (Tinv == null) {
			log.debug("GCoord::barycentric failed " + c1 + " " + c2 + " " + c3 + " " + c4);
			return null; // invalid
		}
		subtract(c4);
		preMultiply(Tinv);
		return this;
	}

}
