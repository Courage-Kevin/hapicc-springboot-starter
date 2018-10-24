package com.hapicc.common.json;

import java.io.IOException;

public interface MessageFormatter {

    String format(Object object) throws IOException;

    byte[] formatAsBytes(Object object) throws IOException;
}
