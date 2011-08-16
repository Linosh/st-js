/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package test;

public class Enums1 {
	public enum Value {
		value1, value2;
	}

	public void main() {
		String s = "x";
		if (s.equals("y")) {
			// test
		}
		Value x = Value.value1;
		switch (x) {
		case value1:
			break;
		case value2:
			break;
		}
	}
}
