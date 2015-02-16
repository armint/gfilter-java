package org.firepick.gfilter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import lombok.extern.log4j.Log4j;

@Log4j
public class MappedPointFilter extends GFilterBase {
	public final static double WEIGHTING_DISTANCE = 0.001; /* All points within this distance are treated the same */

	private GCoordinate domain; // current input domain position
	private double domainRadius;
	private Map<GCoordinate, MappedPoint> mapping;

	protected MappedPointFilter(GFilter next) {
		super(next);
		_name = "MappedPointFilter";
		domainRadius = 0;
		domain = new GCoordinate(0, 0, 0);
		log.info("MappedPointFilter()");
		mapping = new TreeMap<GCoordinate, MappedPoint>();
	}

	public int configure() {
		return 0;
	}

	public GCoordinate interpolate(GCoordinate domainXYZ) {
		switch (mapping.size()) {
		case 0: // No transformation
			log.trace("no interpolation mapping");
			return domain;
		case 1: // a single mapped point defines a universal translation
			GCoordinate result = new GCoordinate(domain);
			return result.add(mapping.entrySet().iterator().next().getValue().getRange()).subtract(mapping.entrySet().iterator().next().getValue().getDomain());
			// return domain + mapping.begin()->second.range - mapping.begin()->second.domain;
		case 2:
			log.error("2-point mapping is undefined"); // translate and scale?
			assert (false);
			break;
		case 3:
			log.error("3-point mapping is undefined"); // translate, scale and rotate?
			assert (false);
			break;
		default: // Point cloud interpolation
			break;
		}

		// interpolate point cloud using simplex barycentric interpolation
//		double maxDist2 = domainRadius * domainRadius;
		ArrayList<MappedPoint> neighborhood = domainNeighborhood(domain, domainRadius);
		log.debug("interpolate(" + domain + ") neighborhood:" + (int) neighborhood.size());

		GCoordinate range = null;
		int n = neighborhood.size();
		if (log.isTraceEnabled()) {
			for (int i = 0; i < n; i++) {
				log.trace("neighborhood[" + i + "]: " + neighborhood.get(i) + " " + domain.distance2(neighborhood.get(i).getDomain()));
			}
		}
		switch (neighborhood.size()) {
		case 0: // no mapping => no change
			range = domain;
			break;
		case 1:
		case 2:
		case 3:
			// weighted average
			break;
		default: // the first four are the closest and are used as the tetrahedron vertices
		case 4: {
			GCoordinate bc = domain.barycentric(neighborhood.get(0).getDomain(), neighborhood.get(1).getDomain(), neighborhood.get(2).getDomain(), neighborhood.get(3).getDomain());
			if (bc.isValid()) {
				double bc4 = 1 - (bc.getX() + bc.getY() + bc.getZ());
				range = neighborhood.get(0).getRange().preMultiply(bc.getX()).add(neighborhood.get(1).getRange().preMultiply(bc.getY()))
						.add(neighborhood.get(2).getRange().preMultiply(bc.getZ())).add(neighborhood.get(3).getRange().preMultiply(bc4));
				range.trunc(5);
				// LOGTRACE4("barycentric(%g,%g,%g,%g)", bc.getX(), bc.getY(), bc.getZ(), bc4);
				// LOGTRACE3("barycentric => (%g,%g,%g)", range.getX(), range.getY(), range.getZ());
			} else {
				log.debug("degenerate tetrahedron");
			}
			break;
		}
		}

		if (range == null || !range.isValid()) {
			// cout << "weighted average" << endl;
			int n1 = Math.min(4, neighborhood.size());
			double[] w = new double[4];
			double wt = 0;
			range = new GCoordinate(0, 0, 0);
			for (int i = 0; i < n1; i++) {
				double d = domain.distance2(neighborhood.get(i).getDomain());
				d = Math.max(d, WEIGHTING_DISTANCE * WEIGHTING_DISTANCE);
				w[i] = 1 / Math.sqrt(d);
				wt += w[i];
			}
			if (wt != 0) {
				for (int i = 0; i < n1; i++) {
					range.add(neighborhood.get(i).getRange().preMultiply(w[i] / wt));
				}
			}
			log.trace("weightd => (" + range.getX() + ", " + range.getY() + ", " + range.getZ() + ")");
		}

		return range;

	}

	public ArrayList<MappedPoint> domainNeighborhood(GCoordinate domainXYZ, double radius) {
		double maxDist2 = radius * radius;
		ArrayList<MappedPoint> neighborhood = new ArrayList<MappedPoint>();
		double dist[] = new double[4]; // sort top 4 for barycentric tetrahedron
		for (int i = 0; i < 4; i++) {
			dist[i] = Double.MAX_VALUE;
		}
		for (Iterator<Map.Entry<GCoordinate, MappedPoint>> ipo = mapping.entrySet().iterator(); ipo.hasNext();) {
			Entry<GCoordinate, MappedPoint> entry = ipo.next();
			double dist2 = domain.distance2(entry.getKey());
			if (dist2 < maxDist2) {
				boolean inserted = false;
				int n = Math.min(4, (int) neighborhood.size() + 1);
				for (int i = 0; i < n; i++) {
					if (dist2 < dist[i]) { // insert here
//						if (dist[i] < Double.MAX_VALUE) {
//						} else {
//						}
						neighborhood.add(i, entry.getValue());
						for (int j = n; --j > i;) {
							dist[j] = dist[j - 1];
						}
						dist[i] = dist2;
						inserted = true;
						break;
					}
				}
				if (!inserted) {
					neighborhood.add(entry.getValue());
				}
			}
		}

		return neighborhood;

	}

	public void mapPoint(GCoordinate domain, GCoordinate range) {
		if (domainRadius == 0.0 || mapping.size() == 0) {
			domainRadius = Math.sqrt(domain.getNorm2());
			log.info("MappedPointFilter() domainRadius:" + domainRadius);
		}

		MappedPoint po = mapping.get(domain);
		po.setDomain(domain);
		po.setRange(range);
	}

	public double getDomainRadius() {
		return domainRadius;
	}

	public void setDomainRadius(double value) {
		domainRadius = value;
	}

}
