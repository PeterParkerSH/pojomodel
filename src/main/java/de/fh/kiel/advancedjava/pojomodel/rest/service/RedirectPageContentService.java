package de.fh.kiel.advancedjava.pojomodel.rest.service;

import org.springframework.stereotype.Service;

@Service
public class RedirectPageContentService {
    public String getRedirectPage(){
    return """
        <!DOCTYPE html>
        <html>
        <body>
            <h2>Success - Redirect to index</h2>
            <script>
                location.replace("/index")
            </script>
        </body>
        </html>
    """;
    }
}
