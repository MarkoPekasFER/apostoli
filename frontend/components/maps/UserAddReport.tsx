import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Input } from "../ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../ui/select";
import { Label } from "../ui/label";
import { Button } from "../ui/button";
import { useEffect, useState } from "react";
import { Plus } from "lucide-react";

function UserAddReport() {
  const [form, setForm] = useState({
    opis: "",
    tip: "",
  });

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    console.log(form);
    const res = await fetch("/api/add/report", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(form),
    });
    const data = await res.json();
    if (data.message === "success") {
      alert("Nesreca je uspjesno prijavljena");
    }
  };

  return (
    <Dialog>
      <DialogTrigger className="p-4 bg-white text-neutral-900 shadow-2xl rounded-full">
        <Plus />
      </DialogTrigger>
      <DialogContent className="bg-white">
        <form onSubmit={handleSubmit}>
          <DialogHeader>
            <DialogTitle>Prijavi Nesrecu</DialogTitle>
            <DialogDescription>Dodaj podatke o nesreci</DialogDescription>
          </DialogHeader>
          <div className="flex flex-col gap-2 pt-4">
            <div className="flex flex-col space-y-1.5">
              <Label htmlFor="opis">Kratki opis</Label>
              <Input
                id="opis"
                placeholder="Kratko opisite nesrecu"
                onChange={(e) => setForm({ ...form, opis: e.target.value })}
              />
            </div>
            <div className="flex flex-col space-y-1.5">
              <Label htmlFor="slika">Upload slike</Label>
              <Input
                id="slika"
                type="file"
                onChange={(e) => setForm({ ...form, opis: e.target.value })}
              />
            </div>
            <div className="flex flex-col space-y-1.5">
              <Label htmlFor="slika">Izaberi lokaciju</Label>
              <div className="w-full h-40 bg-neutral-100 rounded-md">
              </div>
            </div>
            <div className="grid w-full items-center gap-4 pb-4">
              <div className="flex flex-col space-y-1.5">
                <Label htmlFor="tip">Tip nesrece</Label>
                <Select
                  onValueChange={(value) => setForm({ ...form, tip: value })}
                >
                  <SelectTrigger id="tip">
                    <SelectValue placeholder="Select" />
                  </SelectTrigger>
                  <SelectContent position="popper">
                    <SelectItem value="prometna">Prometna nesreca</SelectItem>
                    <SelectItem value=" pozar">Pozar</SelectItem>
                    <SelectItem value="potres">Potres</SelectItem>
                    <SelectItem value="vremenska">Vremenska Nepogoda</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </div>
          </div>
          <DialogFooter>
            <Button type="submit">Prijavi</Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}

export default UserAddReport;
