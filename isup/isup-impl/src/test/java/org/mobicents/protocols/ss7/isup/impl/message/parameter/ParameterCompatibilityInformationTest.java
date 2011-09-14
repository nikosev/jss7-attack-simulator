/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

/**
 * Start time:17:28:44 2009-04-26<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.InstructionIndicators;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import org.testng.*;
import org.testng.annotations.*;
/**
 * Start time:17:28:44 2009-04-26<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class ParameterCompatibilityInformationTest extends ParameterHarness{

	private static final boolean[][] flags = new boolean[4][5];
	private static final int[][] intFlags = new int[4][2];
	private static final int _TRANSIT_EXCHANGE_INDEX = 0;
	private static final int _RLEASE_CALL_INDEX = 1;
	private static final int _SEND_NOTIFICATION_INDEX = 2;
	private static final int _DICARD_MESSAGE_INDEX = 3;
	private static final int _DISCARD_PARAMETER_INDEX = 4;
	private static final int _PASS_NOT_POSSIBLE_INDEX = 0;
	private static final int _BAND_INTERWORKING_INDEX = 1;
	
	static
	{
		intFlags[0][_BAND_INTERWORKING_INDEX] = 3;
		//1
		intFlags[0][_PASS_NOT_POSSIBLE_INDEX]=1;
		//0
		flags[0][_DISCARD_PARAMETER_INDEX]=false;
		//1
		flags[0][_DICARD_MESSAGE_INDEX]=true;
		//0
		flags[0][_SEND_NOTIFICATION_INDEX]=false;
		//1
		flags[0][_RLEASE_CALL_INDEX]=true;
		//1
		flags[0][_TRANSIT_EXCHANGE_INDEX]=true;
		
		//index = 1, its raw, we dont include
		
		//101111
		intFlags[2][_BAND_INTERWORKING_INDEX] = 1;
		//1
		intFlags[2][_PASS_NOT_POSSIBLE_INDEX]=1;
		//0
		flags[2][_DISCARD_PARAMETER_INDEX]=false;
		//1
		flags[2][_DICARD_MESSAGE_INDEX]=true;
		//1
		flags[2][_SEND_NOTIFICATION_INDEX]=true;
		//1
		flags[2][_RLEASE_CALL_INDEX]=true;
		//1
		flags[2][_TRANSIT_EXCHANGE_INDEX]=true;
		
		//111011
		intFlags[3][_BAND_INTERWORKING_INDEX] = 2;
		//1
		intFlags[3][_PASS_NOT_POSSIBLE_INDEX]=1;
		//1
		flags[3][_DISCARD_PARAMETER_INDEX]=true;
		//1
		flags[3][_DICARD_MESSAGE_INDEX]=true;
		//0
		flags[3][_SEND_NOTIFICATION_INDEX]=false;
		//1
		flags[3][_RLEASE_CALL_INDEX]=true;
		//1
		flags[3][_TRANSIT_EXCHANGE_INDEX]=true;
		
		
	}
	
	public ParameterCompatibilityInformationTest() {
		super();
		super.goodBodies.add(getBody1());
	}

	@Test(groups = { "functional.encode","functional.decode","parameter"})
	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterException {
		ParameterCompatibilityInformationImpl bci = new ParameterCompatibilityInformationImpl(getBody1());
	
		assertEquals(bci.size(),4, "Wrong number of instructions. ");
		
		//Yeah this is different
		
		for(int index = 0 ; index< bci.size() ; index++)
		{
			//this is raw, we dotn validate
			if(index ==1)
				continue;
			
			InstructionIndicators ii = bci.getInstructionIndicators(index);
			
			String[] methodNames = { "getBandInterworkingIndicator", 
									 "getPassOnNotPossibleIndicator", 
									 "isDiscardParameterIndicator", 
									 "isDiscardMessageIndicator", 
									 "isSendNotificationIndicator",
									 "isReleaseCallindicator",
									 "isTransitAtIntermediateExchangeIndicator"};
		
			Object[] expectedValues = { 
			intFlags[index][_BAND_INTERWORKING_INDEX] ,
			//1
			intFlags[index][_PASS_NOT_POSSIBLE_INDEX],
			//0
			flags[index][_DISCARD_PARAMETER_INDEX],
			//1
			flags[index][_DICARD_MESSAGE_INDEX],
			//0
			flags[index][_SEND_NOTIFICATION_INDEX],
			//1
			flags[index][_RLEASE_CALL_INDEX],
			//1
			flags[index][_TRANSIT_EXCHANGE_INDEX]};
			super.testValues((AbstractISUPParameter)ii, methodNames, expectedValues);
			
		}
		
		
		
		byte parameterCode = bci.getParameterCode(0);
		assertEquals( parameterCode,1,"Wrong parameter code");
		
		
		
	}
	
	
	private byte[] getBody1() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		bos.write(1);
		bos.write(0x80 | 0x2B);
		bos.write(3);
		
		bos.write(2);
		bos.write(0x80 | 0x2B);
		bos.write(0x80 | 3);
		bos.write(12);
		
		bos.write(3);
		bos.write(0x80 | 0x2F);
		bos.write(1);
		
		bos.write(4);
		bos.write(0x80 | 0x3B);
		bos.write(2);
		return bos.toByteArray();
	}


	
	public AbstractISUPParameter getTestedComponent() {
		return new ParameterCompatibilityInformationImpl();
	}

}
