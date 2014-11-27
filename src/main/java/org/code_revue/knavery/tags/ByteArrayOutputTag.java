package org.code_revue.knavery.tags;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * @author Mike Fanning
 */
public class ByteArrayOutputTag extends SimpleTagSupport {

    private byte[] value;

    private String var = null;

    private String outputType = "hex";

    private String separator = " ";

    private boolean pad = true;

    @Override
    public void doTag() throws IOException {
        StringBuilder builder = new StringBuilder(value.length * (3 + separator.length()));
        boolean first = true;
        for (byte b: value) {
            if (first) {
                first = false;
            } else if (null != separator && !"".equals(separator)) {
                builder.append(separator);
            }

            if ("dec".equals(outputType)) {
                if (pad) {
                    if (b >= 0 && b < 10) {
                        builder.append("00");
                    } else if (b >= 0 && b < 100) {
                        builder.append('0');
                    }
                }
                builder.append(Integer.toString(b & 0xff));
            } else {
                if (pad && b >= 0 && b < 16) {
                    builder.append('0');
                }
                builder.append(Integer.toHexString(b).replace("ffffff", ""));
            }
        }

        if (null != var) {
            getJspContext().setAttribute(var, builder.toString());
        } else {
            getJspContext().getOut().append(builder.toString());
        }
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public void setPad(boolean pad) {
        this.pad = pad;
    }
}
