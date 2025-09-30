import { Request, Response } from "express";

function formatURL(baseURL: string, linkString: string): string {
  // Check if linkString has a protocol
  if (linkString.includes(':')) {
    try {
      const url = new URL(linkString);
      // Return as-is if not http/https
      if (url.protocol !== 'http:' && url.protocol !== 'https:') {
        return linkString;
      }
    } catch {
      // Not a valid URL, continue processing
    }
  }

  const base = new URL(baseURL);

  // If linkString starts with '/', resolve from origin
  if (linkString.startsWith('/')) {
    return new URL(linkString, base.origin).href;
  }

  // Otherwise, resolve relative to the baseURL's directory
  return new URL(linkString, baseURL).href;
}

export const FormatURL = async (req: Request, res: Response) => {
  const { baseURL, linkString } = req.body;

  if (!baseURL || !linkString) {
    return res.status(400).json({ error: "baseURL and linkString are required" });
  }

  try {
    const result = formatURL(baseURL, linkString);
    res.json({ url: result });
  } catch (error) {
    res.status(500).json({ error: "Failed to format URL" });
  }
};