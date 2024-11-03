// Next.js API route support: https://nextjs.org/docs/api-routes/introduction
import type { NextApiRequest, NextApiResponse } from "next";

type Data = {
  lat: number;
  lng: number;
  name: string;
}[];

export default function handler(
  req: NextApiRequest,
  res: NextApiResponse<Data>,
) {
  res.status(200).json([
    { lat: 45.81501, lng: 15.981919, name: "Location 1" },
    { lat: 45.796688, lng: 15.98225, name: "Location 2" },
    { lat: 45.777296, lng: 15.952892, name: "Location 3" },
    { lat: 45.803016, lng: 15.978817, name: "Location 4" },
  ]);
}
