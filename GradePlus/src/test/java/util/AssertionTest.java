/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 AG Softwaretechnik, University of Bremen:
 * Karsten Hölscher, Sebastian Offermann, Dennis Schürholz, Marcel Steinbeck
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights 
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package util;

import static common.util.Assertion.assertNotNegative;
import static common.util.Assertion.assertWithoutEmpty;
import static common.util.Assertion.assertWithoutNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;

/**
 * Äquivalenz- und Grenztests für die Methoden der Klasse {@link common.util.Assertion}.
 * 
 * @author Sebastian Offermann, Karsten Hölscher
 * @version 2017-07-03
 */
public class AssertionTest {

    @Test
    public void testValidSizePositive() {
        final int input = 95;
        final int result = assertNotNegative(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void testValidSizeZero() {
        final int input = 0;
        final int result = assertNotNegative(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void testValidSizeNegative() {
        final int input = -1;
        assertThatIllegalArgumentException().isThrownBy(() -> assertNotNegative(input));
    }

    @Test
    public void testNotNullString() {
        final String input = "a";
        final String result = assertWithoutNull(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void testNotNullStringNull() {
        final String input = null;
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutNull(input));
    }

    @Test
    public void testNotNullStringWhite() {
        final String input = " \t";
        final String result = assertWithoutNull(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void testNotNullObject() {
        final Object input = new Object();
        final Object result = assertWithoutNull(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void testNotNullObjectNull() {
        final Object input = null;
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutNull(input));
    }

    @Test
    public void testNotNullStringCollection() {
        final List<String> input = new ArrayList<>(Arrays.asList("Gunther", "Herbert"));
        final List<String> result = assertWithoutNull(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void testNotNullStringCollectionNull() {
        final List<String> input = null;
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutNull(input));
    }

    @Test
    public void testNotNullStringCollectionContainsNull() {
        final List<String> input = new ArrayList<>(Arrays.asList("Gunther", null));
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutNull(input));
    }

    @Test
    public void testNotNullFixedLengthList() {
        final List<String> input = Arrays.asList("Gunther", "Herbert");
        final List<String> result = assertWithoutNull(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void testNotNullFixedLengthListContainsNull() {
        final List<String> input = Arrays.asList("Gunther", null);
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutNull(input));
    }

    @Test
    public void testNotNullStringArray() {
        final String[] input = new String[] { "Gunther", "Herbert" };
        final String[] result = assertWithoutNull(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void testNotNullStringArrayNull() {
        final String[] input = null;
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutNull(input));
    }

    @Test
    public void testNotNullStringArrayContainsNull() {
        final String[] input = new String[] { "Gunther", null };
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutNull(input));
    }

    @Test
    public void testNotNullNestedCollection() {
        final List<List<String>> input = new ArrayList<>();
        input.add(new ArrayList<>(Arrays.asList("Gunther", "Herbert")));
        input.add(new ArrayList<>(Arrays.asList("Myra", "Jane", "Thomas")));
        input.add(new ArrayList<>(Arrays.asList("Lauren")));
        final List<List<String>> result = assertWithoutNull(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void testNotNullNestedCollectionNull() {
        final List<List<String>> input = null;
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutNull(input));
    }

    @Test
    public void testNotNullNestedCollectionContainsNull() {
        final List<List<String>> input = new ArrayList<>();
        input.add(new ArrayList<>(Arrays.asList("Gunther", "Herbert")));
        input.add(null);
        input.add(new ArrayList<>(Arrays.asList("Lauren")));
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutNull(input));
    }

    @Test
    public void testNotNullNestedCollectionContainsNullInDepth() {
        final List<List<String>> input = new ArrayList<>();
        input.add(new ArrayList<>(Arrays.asList("Gunther", "Herbert")));
        input.add(new ArrayList<>(Arrays.asList("Myra", null, "Thomas")));
        input.add(new ArrayList<>(Arrays.asList("Lauren")));
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutNull(input));
    }

    @Test
    public void testNotNullNestedCollectionContainsEmptyStringInDepth() {
        final List<List<String>> input = new ArrayList<>();
        input.add(new ArrayList<>(Arrays.asList("Gunther", "Herbert")));
        input.add(new ArrayList<>(Arrays.asList("Myra", "Jane", "")));
        input.add(new ArrayList<>(Arrays.asList("Lauren")));
        final List<List<String>> result = assertWithoutNull(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void testNotNullNestedArray() {
        final List<String[]> input = new ArrayList<>();
        input.add(new String[] { "Gunther", "Herbert" });
        input.add(new String[] { "Myra", "Jane", "Thomas" });
        input.add(new String[] { "Lauren" });
        final List<String[]> result = assertWithoutNull(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void testNotNullNestedArrayNull() {
        final List<String[]> input = null;
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutNull(input));
    }

    @Test
    public void testNotNullNestedArrayContainsNull() {
        final List<String[]> input = new ArrayList<>();
        input.add(new String[] { "Gunther", "Herbert" });
        input.add(null);
        input.add(new String[] { "Lauren" });
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutNull(input));
    }

    @Test
    public void testNotNullNestedArrayContainsNullInDepth() {
        final List<String[]> input = new ArrayList<>();
        input.add(new String[] { "Gunther", "Herbert" });
        input.add(new String[] { "Myra", null, "Thomas" });
        input.add(new String[] { "Lauren" });
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutNull(input));
    }

    @Test
    public void testNotNullNestedArrayContainsEmptyStringInDepth() {
        final List<String[]> input = new ArrayList<>();
        input.add(new String[] { "Gunther", "Herbert" });
        input.add(new String[] { "Myra", "Jane", "" });
        input.add(new String[] { "Lauren" });
        final List<String[]> result = assertWithoutNull(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void testNotNullDeepNestedCollection() throws InstantiationException,
            IllegalAccessException {
        final Integer[][][] elements = { { { 27, -45 }, { 89, 340, 35 } },
                { { 398, 95, 2034 }, { 9467585, 34, -53 }, { -3456 } } };
        @SuppressWarnings("unchecked")
        final List<Set<List<Integer>>> input = To.collection(elements, ArrayList.class,
                HashSet.class, ArrayList.class);
        final List<Set<List<Integer>>> result = assertWithoutNull(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void testNotNullDeepNestedCollectionNull() {
        final List<Set<List<Integer>>> input = null;
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutNull(input));
    }

    @Test
    public void testNotNullDeepNestedCollectionContainsNull()
            throws InstantiationException, IllegalAccessException {
        final Integer[][][] elements = { { { 27, -45 }, { 89, 340, 35 } }, null,
                { { 398, 95, 2034 }, { 9467585, 34, -53 }, { -3456 } } };
        @SuppressWarnings("unchecked")
        final List<Set<List<Integer>>> input = To.collection(elements, ArrayList.class,
                HashSet.class, ArrayList.class);
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutNull(input));
    }

    @Test
    public void testNotNullDeepNestedCollectionContainsNullinMidLevel()
            throws InstantiationException, IllegalAccessException {
        final Integer[][][] elements = { { { 27, -45 }, { 89, 340, 35 }, null },
                { { 398, 95, 2034 }, { 9467585, 34, -53 }, { -3456 } } };
        @SuppressWarnings("unchecked")
        final List<Set<List<Integer>>> input = To.collection(elements, ArrayList.class,
                HashSet.class, ArrayList.class);
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutNull(input));
    }

    @Test
    public void testNotNullDeepNestedCollectionContainsNullInDepth()
            throws InstantiationException, IllegalAccessException {
        final Integer[][][] elements = { { { 27, -45 }, { 89, 340, 35 } },
                { { 398, 95, 2034 }, { 9467585, null, -53 }, { -3456 } } };
        @SuppressWarnings("unchecked")
        final List<Set<List<Integer>>> input = To.collection(elements, ArrayList.class,
                HashSet.class, ArrayList.class);
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutNull(input));
    }

    @Test
    public void testNotEmptyString() {
        final String input = "a";
        final String result = assertWithoutEmpty(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void testNotEmptyStringNull() {
        final String input = null;
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutEmpty(input));
    }

    @Test
    public void testNotEmptyStringWhite() {
        final String input = " \t";
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutEmpty(input));
    }

    @Test
    public void testNotEmptyObject() {
        final Object input = new Object();
        final Object result = assertWithoutEmpty(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void testNotEmptyObjectNull() {
        final Object input = null;
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutEmpty(input));
    }

    @Test
    public void testNotEmptyStringCollection() {
        final List<String> input = new ArrayList<>(Arrays.asList("Gunther", "Herbert"));
        final List<String> result = assertWithoutEmpty(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void testNotEmptyStringCollectionNull() {
        final List<String> input = null;
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutEmpty(input));
    }

    @Test
    public void testNotEmptyStringCollectionContainsNull() {
        final List<String> input = new ArrayList<>(Arrays.asList("Gunther", null));
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutEmpty(input));
    }

    @Test
    public void testNotEmptyFixedLengthList() {
        final List<String> input = Arrays.asList("Gunther", "Herbert");
        final List<String> result = assertWithoutEmpty(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void testNotEmptyFixedLengthListContainsNull() {
        final List<String> input = Arrays.asList("Gunther", null);
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutEmpty(input));
    }

    @Test
    public void testNotEmptyStringArray() {
        final String[] input = new String[] { "Gunther", "Herbert" };
        final String[] result = assertWithoutEmpty(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void testNotEmptyStringArrayNull() {
        final String[] input = null;
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutEmpty(input));
    }

    @Test
    public void testNotEmptyStringArrayContainsNull() {
        final String[] input = new String[] { "Gunther", null };
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutEmpty(input));
    }

    @Test
    public void testNotEmptyNestedCollection() {
        final List<List<String>> input = new ArrayList<>();
        input.add(new ArrayList<>(Arrays.asList("Gunther", "Herbert")));
        input.add(new ArrayList<>(Arrays.asList("Myra", "Jane", "Thomas")));
        input.add(new ArrayList<>(Arrays.asList("Lauren")));
        final List<List<String>> result = assertWithoutEmpty(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void testNotEmptyNestedCollectionNull() {
        final List<List<String>> input = null;
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutEmpty(input));
    }

    @Test
    public void testNotEmptyNestedCollectionContainsNull() {
        final List<List<String>> input = new ArrayList<>();
        input.add(new ArrayList<>(Arrays.asList("Gunther", "Herbert")));
        input.add(null);
        input.add(new ArrayList<>(Arrays.asList("Lauren")));
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutEmpty(input));
    }

    @Test
    public void testNotEmptyNestedCollectionContainsNullInDepth() {
        final List<List<String>> input = new ArrayList<>();
        input.add(new ArrayList<>(Arrays.asList("Gunther", "Herbert")));
        input.add(new ArrayList<>(Arrays.asList("Myra", null, "Thomas")));
        input.add(new ArrayList<>(Arrays.asList("Lauren")));
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutEmpty(input));
    }

    @Test
    public void testNotEmptyNestedCollectionContainsEmptyStringInDepth() {
        final List<List<String>> input = new ArrayList<>();
        input.add(new ArrayList<>(Arrays.asList("Gunther", "Herbert")));
        input.add(new ArrayList<>(Arrays.asList("Myra", "Jane", "")));
        input.add(new ArrayList<>(Arrays.asList("Lauren")));
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutEmpty(input));
    }

    @Test
    public void testNotEmptyNestedArray() {
        final List<String[]> input = new ArrayList<>();
        input.add(new String[] { "Gunther", "Herbert" });
        input.add(new String[] { "Myra", "Jane", "Thomas" });
        input.add(new String[] { "Lauren" });
        final List<String[]> result = assertWithoutEmpty(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void testNotEmptyNestedArrayNull() {
        final List<String[]> input = null;
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutEmpty(input));
    }

    @Test
    public void testNotEmptyNestedArrayContainsNull() {
        final List<String[]> input = new ArrayList<>();
        input.add(new String[] { "Gunther", "Herbert" });
        input.add(null);
        input.add(new String[] { "Lauren" });
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutEmpty(input));
    }

    @Test
    public void testNotEmptyNestedArrayContainsNullInDepth() {
        final List<String[]> input = new ArrayList<>();
        input.add(new String[] { "Gunther", "Herbert" });
        input.add(new String[] { "Myra", null, "Thomas" });
        input.add(new String[] { "Lauren" });
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutEmpty(input));
    }

    @Test
    public void testNotEmptyNestedArrayContainsEmptyStringInDepth() {
        final List<String[]> input = new ArrayList<>();
        input.add(new String[] { "Gunther", "Herbert" });
        input.add(new String[] { "Myra", "Jane", "" });
        input.add(new String[] { "Lauren" });
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutEmpty(input));
    }

    @Test
    public void testNotEmptyDeepNestedCollection() throws InstantiationException,
            IllegalAccessException {
        final Integer[][][] elements = { { { 27, -45 }, { 89, 340, 35 } },
                { { 398, 95, 2034 }, { 9467585, 34, -53 }, { -3456 } } };
        @SuppressWarnings("unchecked")
        final List<Set<List<Integer>>> input = To.collection(elements, ArrayList.class,
                HashSet.class, ArrayList.class);
        final List<Set<List<Integer>>> result = assertWithoutEmpty(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void testNotEmptyDeepNestedCollectionNull() {
        final List<Set<List<Integer>>> input = null;
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutEmpty(input));
    }

    @Test
    public void testNotEmptyDeepNestedCollectionContainsNull()
            throws InstantiationException, IllegalAccessException {
        final Integer[][][] elements = { { { 27, -45 }, { 89, 340, 35 } }, null,
                { { 398, 95, 2034 }, { 9467585, 34, -53 }, { -3456 } } };
        @SuppressWarnings("unchecked")
        final List<Set<List<Integer>>> input = To.collection(elements, ArrayList.class,
                HashSet.class, ArrayList.class);
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutEmpty(input));
    }

    @Test
    public void testNotEmptyDeepNestedCollectionContainsNullinMidLevel()
            throws InstantiationException, IllegalAccessException {
        final Integer[][][] elements = { { { 27, -45 }, { 89, 340, 35 }, null },
                { { 398, 95, 2034 }, { 9467585, 34, -53 }, { -3456 } } };
        @SuppressWarnings("unchecked")
        final List<Set<List<Integer>>> input = To.collection(elements, ArrayList.class,
                HashSet.class, ArrayList.class);
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutEmpty(input));
    }

    @Test
    public void testNotEmptyDeepNestedCollectionContainsNullInDepth()
            throws InstantiationException, IllegalAccessException {
        final Integer[][][] elements = { { { 27, -45 }, { 89, 340, 35 } },
                { { 398, 95, 2034 }, { 9467585, null, -53 }, { -3456 } } };
        @SuppressWarnings("unchecked")
        final List<Set<List<Integer>>> input = To.collection(elements, ArrayList.class,
                HashSet.class, ArrayList.class);
        assertThatIllegalArgumentException().isThrownBy(() -> assertWithoutEmpty(input));
    }

}
