package ru.homeless.filters;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Special filter that adds encoding information to filename in Content-Disposition
 * @author lans
 */
@WebFilter(filterName = "ContentDispositionFilter", urlPatterns = {"*.xhtml"})
public class ContentDispositionFilter implements Filter {

    public ContentDispositionFilter() {
    }

    public static class EncodedFilenameWrapper extends HttpServletResponseWrapper {

        private static final Pattern UNICODE_FILENAME
                = Pattern.compile("filename\\s*=\\s*\"(.*)\"$");

        public EncodedFilenameWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public void setHeader(String name, String value) {
            if (name.equals("Content-Disposition")) {
                Matcher names = UNICODE_FILENAME.matcher(value);
                value = names.replaceAll("filename*=UTF-8''$1");
            }
            super.setHeader(name, value);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        chain.doFilter(request, new EncodedFilenameWrapper((HttpServletResponse) response));
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {
    }
}
