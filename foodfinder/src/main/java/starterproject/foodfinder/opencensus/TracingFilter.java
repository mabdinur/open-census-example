package starterproject.foodfinder.opencensus;

import io.opencensus.common.Scope;
import io.opencensus.trace.Span;
import io.opencensus.trace.SpanBuilder;
import io.opencensus.trace.SpanContext;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class TracingFilter extends OncePerRequestFilter {

	private static final Logger LOG = Logger.getLogger(TracingFilter.class.getName());
    private static final Tracer tracer = Tracing.getTracer();
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String spanName = "START " + request.getMethod() + " " + request.getRequestURI();
        Span span = SpanUtils.buildSpan(tracer, spanName).startSpan();
        
        try (Scope s = tracer.withSpan(span)) {
            filterChain.doFilter(request, response);
        }
        
	 	span.end();
    }
}
