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

import omero.gateway.model.RectangleData;

/**
 * An {@link OMERORectangle} with open boundary behavior.
 *
 * @author Alison Walter
 */
public class OpenOMERORectangle extends AbstractOMEROShape<RectangleData>
	implements OMERORectangle
{

	public OpenOMERORectangle(final RectangleData shape) {
		super(shape, BoundaryType.OPEN);
	}

	@Override
	public boolean test(final RealLocalizable l) {
		final double lx = l.getDoublePosition(0);
		final double ly = l.getDoublePosition(1);

		final double minX = shape.getX();
		final double minY = shape.getY();
		final double maxX = minX + shape.getWidth();
		final double maxY = minY + shape.getHeight();

		return lx > minX && lx < maxX && ly > minY && ly < maxY;
	}

	@Override
	public RealPoint center() {
		return new RectangleCenter(new double[] { getShape().getX() + getShape()
			.getWidth() / 2, getShape().getY() + getShape().getHeight() / 2 });
	}

	// -- Helper classes --

	private class RectangleCenter extends AbstractRealMaskPoint {

		public RectangleCenter(final double[] pos) {
			super(pos);
		}

		@Override
		public void updateBounds() {
			// Bounds depend on wrapped OMERO shape, so by updating the shape we're
			// updating the bounds
			shape.setX(position[0] - shape.getWidth() / 2);
			shape.setY(position[1] - shape.getHeight() / 2);
		}

	}

}