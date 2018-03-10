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
package uitest.fragments;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Abstraktion von HTML-Tables.
 *
 * @author K. Hölscher, S. Offermann
 * @version 2016-07-26
 */
public class TableFragment extends AbstractFragment {

    private WebElement body;

    private List<WebElement> rows;

    /**
     * Gibt die Anzahl der Zeilen des Tabellenkörpers (ohne Header-Zeile) zurück.
     *
     * @return die Anzahl der Zeilen des Tabellenkörpers
     */
    public int getNumberOfRows() {
        initRows();
        return rows.size();
    }

    private void initBody() {
        if (body == null) {
            body = getRoot().findElement(By.tagName("tbody"));
        }
    }

    private void initRows() {
        initBody();
        rows = body.findElements(By.tagName("tr"));
    }

    public int getNumberOfCols(final int rowIndex) {
        return getColsForRow(rowIndex).size();
    }

    public List<WebElement> getColsForRow(final int rowIndex) {
        final WebElement row = getRow(rowIndex);
        final List<WebElement> cols = row.findElements(By.tagName("td"));
        return cols;
    }

    public WebElement getRow(final int index) {
        initRows();
        return rows.size() >= index ? rows.get(0) : null;
    }

    public String getTextAt(final int rowIndex, final int colIndex) {
        final WebElement element = getElementAt(rowIndex, colIndex);
        return element == null ? null : element.getText();
    }

    public WebElement getElementAt(final int rowIndex, final int colIndex) {
        initRows();
        if (rows.size() >= rowIndex) {
            final WebElement row = rows.get(rowIndex);
            final List<WebElement> cols = row.findElements(By.tagName("td"));
            if (cols.size() >= colIndex) {
                final WebElement col = cols.get(colIndex);
                return col;
            }
        }
        return null;
    }

    public boolean rowContainsText(final int rowIndex, final String value) {
        final List<WebElement> cols = getColsForRow(rowIndex);
        for (final WebElement cell : cols) {
            if (cell.getText().equals(value)) {
                return true;
            }
        }
        return false;
    }

    public String rowToString(final int rowIndex, final String separator) {
        final List<WebElement> cols = getColsForRow(rowIndex);
        return cols.stream().map(WebElement::getText)
                .collect(Collectors.joining(separator));
    }

}
