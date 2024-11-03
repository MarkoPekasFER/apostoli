// Next.js API route support: https://nextjs.org/docs/api-routes/introduction
import type { NextApiRequest, NextApiResponse } from "next";


export default function handler(
  req: NextApiRequest,
  res: NextApiResponse,
) {
    if (req.method === "POST") {
        console.log(req.body);
        res.status(200).json({ message: "success" });
    } else {
        res.status(405).json({ message: "Method not allowed" });
    }
}
