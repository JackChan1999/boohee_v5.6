package org.eclipse.mat.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public final class SimpleStringTokenizer implements Iterable<String> {
    private char delim;
    private String subject;

    public SimpleStringTokenizer(String subject, char delim) {
        this.subject = subject;
        this.delim = delim;
    }

    public Iterator<String> iterator() {
        return new Iterator<String>() {
            int maxPosition = SimpleStringTokenizer.this.subject.length();
            int position = 0;

            public boolean hasNext() {
                return this.position < this.maxPosition;
            }

            public String next() {
                if (this.position >= this.maxPosition) {
                    throw new NoSuchElementException();
                }
                int p = SimpleStringTokenizer.this.subject.indexOf(SimpleStringTokenizer.this.delim, this.position);
                if (p < 0) {
                    String answer = SimpleStringTokenizer.this.subject.substring(this.position);
                    this.position = this.maxPosition;
                    return answer;
                }
                answer = SimpleStringTokenizer.this.subject.substring(this.position, p);
                this.position = p + 1;
                return answer;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static String[] split(String subject, char delim) {
        List<String> answer = new ArrayList();
        Iterator i$ = new SimpleStringTokenizer(subject, delim).iterator();
        while (i$.hasNext()) {
            answer.add(((String) i$.next()).trim());
        }
        return (String[]) answer.toArray(new String[0]);
    }
}
