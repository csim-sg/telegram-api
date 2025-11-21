package com.bw.microapp.service;

import org.springframework.stereotype.Service;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Service
public class UrlService {

  public String formatURL(String baseURL, String linkString) {
    // Remove hashtag
    String cleanLink = linkString.split("#")[0];

    // Check if linkString has a protocol
    if (cleanLink.contains(":")) {
      try {
        URL url = new URI(cleanLink).toURL();
        // Return as-is if not http/https
        if (!url.getProtocol().equals("http") && !url.getProtocol().equals("https")) {
          return cleanLink;
        }
      } catch (MalformedURLException | URISyntaxException e) {
        // Not a valid URL, continue processing
      } catch (IllegalArgumentException e) {
        // URI syntax error
      }
    }

    try {
      URL base = new URI(baseURL).toURL();

      // If linkString starts with '/', resolve from origin
      if (cleanLink.startsWith("/")) {
        // Construct origin from base URL
        String origin = base.getProtocol() + "://" + base.getHost();
        if (base.getPort() != -1) {
          origin += ":" + base.getPort();
        }
        return new URI(origin).resolve(cleanLink).toURL().toString();
      }

      // Otherwise, resolve relative to the baseURL's directory
      return base.toURI().resolve(cleanLink).toURL().toString();

    } catch (MalformedURLException | URISyntaxException | IllegalArgumentException e) {
      // Fallback or error handling
      return cleanLink;
    }
  }
}
