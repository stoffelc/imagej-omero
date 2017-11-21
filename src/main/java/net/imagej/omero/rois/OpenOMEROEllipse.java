/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2013 - 2016 Open Microscopy Environment:
 * 	- Board of Regents of the University of Wisconsin-Madison
 * 	- Glencoe Software, Inc.
 * 	- University of Dundee
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

package net.imagej.omero.rois;

import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.roi.BoundaryType;
import net.imglib2.roi.util.AbstractRealMaskPoint;

import omero.gateway.model.EllipseData;

/**
 * An {@link OMEROEllipse} with open boundary behavior.
 *
 * @author Alison Walter
 */
public class OpenOMEROEllipse extends AbstractOMEROShape<EllipseData> implements
	OMEROEllipse
{

	public OpenOMEROEllipse(final EllipseData shape) {
		super(shape, BoundaryType.OPEN);
	}

	@Override
	public boolean test(final RealLocalizable l) {
		final double distanceX = (l.getDoublePosition(0) - shape.getX()) / shape
			.getRadiusX();
		final double distanceY = (l.getDoublePosition(1) - shape.getY()) / shape
			.getRadiusY();

		return (distanceX * distanceX + distanceY * distanceY) < 1.0;
	}

	@Override
	public RealPoint center() {
		return new EllipseCenter(new double[] { shape.getX(), shape.getY() });
	}

	// -- Helper classes

	private class EllipseCenter extends AbstractRealMaskPoint {

		public EllipseCenter(final double[] pos) {
			super(pos);
		}

		@Override
		public void updateBounds() {
			// Bounds depend on wrapped OMERO shape, so by updating the shape we're
			// updating the bounds
			shape.setX(position[0]);
			shape.setY(position[1]);
		}

	}

}