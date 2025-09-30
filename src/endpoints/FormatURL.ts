import { Request, Response } from "express";

function formatURL(baseURL: string, linkString: string): string {
  // Remove hashtag
  const cleanLink = linkString.split('#')[0];

  // Check if linkString has a protocol
  if (cleanLink.includes(':')) {
    try {
      const url = new URL(cleanLink);
      // Return as-is if not http/https
      if (url.protocol !== 'http:' && url.protocol !== 'https:') {
        return cleanLink;
      }
    } catch {
      // Not a valid URL, continue processing
    }
  }

  const base = new URL(baseURL);

  // If linkString starts with '/', resolve from origin
  if (cleanLink.startsWith('/')) {
    return new URL(cleanLink, base.origin).href;
  }

  // Otherwise, resolve relative to the baseURL's directory
  return new URL(cleanLink, baseURL).href;
}

export const FormatURL = async (req: Request, res: Response) => {
  const { baseURL, linkString } = req.body;

  if (!baseURL || !linkString) {
    return res.status(400).json({ error: "baseURL and linkString are required" });
  }

  if (!Array.isArray(linkString)) {
    return res.status(400).json({ error: "linkString must be an array" });
  }

  try {
    const results = linkString.map(link => formatURL(baseURL, link));
    const uniqueResults = results.filter((url, index) => results.indexOf(url) === index);
    res.json({ urls: uniqueResults });
  } catch (error) {
    res.status(500).json({ error: "Failed to format URL" });
  }
};