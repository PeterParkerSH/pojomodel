package de.fh.kiel.advancedjava.pojoapplication.rest.service;

import org.springframework.stereotype.Service;

/**
 * Produces html page that automatically redirects to index page
 */
@Service
public class RedirectPageContentService {
    public String getRedirectPage(){
    return """
        <!DOCTYPE html>
        <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>Pojo Result Page </title>
            </head>

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
