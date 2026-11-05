import { useState, useEffect } from "react";
import { getMothers, registerMother } from "../services/api";
import { Plus, Search, Users, ArrowLeft } from "lucide-react";
import "./Mothers.css";

const emptyForm = {
  fullName: "",
  dateOfBirth: "",
  phone: "",
  county: "",
  subCounty: "",
  facility: "",
  lastMenstrualPeriod: "",
  expectedDeliveryDate: "",
  gravida: "",
  parity: "",
};

export default function Mothers() {
  const [mothers, setMothers] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [search, setSearch] = useState("");
  const [form, setForm] = useState(emptyForm);

  useEffect(() => {
    getMothers()
      .then((data) => setMothers(data))
      .catch((error) => console.error("Failed to load mothers:", error));
  }, []);

  function handleChange(event) {
    const { name, value } = event.target;

    setForm((previous) => ({
      ...previous,
      [name]: value,
    }));
  }

  async function handleSubmit(event) {
    event.preventDefault();

    const mother = {
      fullName: form.fullName.trim(),
      dateOfBirth: form.dateOfBirth,
      phoneNumber: form.phone.trim(),
      county: form.county.trim(),
      subCounty: form.subCounty.trim(),
      lastMenstrualPeriod: form.lastMenstrualPeriod || null,
      expectedDeliveryDate: form.expectedDeliveryDate || null,
      healthFacility: form.facility.trim(),
      gravida: form.gravida === "" ? null : Number(form.gravida),
      parity: form.parity === "" ? null : Number(form.parity),
    };

    try {
      await registerMother(mother);
      const savedMothers = await getMothers();
      setMothers(savedMothers);
      setForm({ ...emptyForm });
      setShowForm(false);
      alert("Mother registered successfully in PostgreSQL!");
    } catch (error) {
      console.error("Registration failed:", error);
      alert("Registration failed: " + error.message);
    }
  }

  const filteredMothers = mothers.filter((mother) =>
    mother.fullName.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="mothers-page">
      <div className="mothers-heading">
        <div>
          <h1>Mother Registration</h1>
          <p>Manage maternal healthcare records and registrations.</p>
        </div>

        <button
          className="mother-primary-btn"
          onClick={() => setShowForm(!showForm)}
        >
          {showForm ? <ArrowLeft size={18} /> : <Plus size={18} />}
          {showForm ? "Back to Records" : "Register Mother"}
        </button>
      </div>

      {showForm ? (
        <form className="mother-form" onSubmit={handleSubmit}>
          <h2>Personal Information</h2>

          <div className="mother-form-grid">
            <label>
              Full Name *
              <input
                name="fullName"
                value={form.fullName}
                onChange={handleChange}
                required
                placeholder="Enter mother's full name"
              />
            </label>

            <label>
              Date of Birth *
              <input
                type="date"
                name="dateOfBirth"
                value={form.dateOfBirth}
                onChange={handleChange}
                max={new Date().toISOString().split("T")[0]}
                required
              />
            </label>

            <label>
              Phone Number
              <input
                type="tel"
                name="phone"
                value={form.phone}
                onChange={handleChange}
                placeholder="07XXXXXXXX"
              />
            </label>

            <label>
              County
              <input
                name="county"
                value={form.county}
                onChange={handleChange}
                placeholder="Enter county"
              />
            </label>

            <label>
              Sub-County
              <input
                name="subCounty"
                value={form.subCounty}
                onChange={handleChange}
                placeholder="Enter sub-county"
              />
            </label>

            <label>
              Health Facility
              <input
                name="facility"
                value={form.facility}
                onChange={handleChange}
                placeholder="Enter health facility"
              />
            </label>
          </div>

          <h2>Pregnancy Information</h2>

          <div className="mother-form-grid">
            <label>
              Last Menstrual Period (LMP)
              <input
                type="date"
                name="lastMenstrualPeriod"
                value={form.lastMenstrualPeriod}
                onChange={handleChange}
                max={new Date().toISOString().split("T")[0]}
              />
            </label>

            <label>
              Expected Delivery Date (EDD)
              <input
                type="date"
                name="expectedDeliveryDate"
                value={form.expectedDeliveryDate}
                onChange={handleChange}
              />
            </label>

            <label>
              Gravida (Number of Pregnancies)
              <input
                type="number"
                min="0"
                name="gravida"
                value={form.gravida}
                onChange={handleChange}
                placeholder="e.g. 2"
              />
            </label>

            <label>
              Parity (Previous Births)
              <input
                type="number"
                min="0"
                name="parity"
                value={form.parity}
                onChange={handleChange}
                placeholder="e.g. 1"
              />
            </label>
          </div>

          <div className="mother-form-actions">
            <button
              type="button"
              className="mother-cancel-btn"
              onClick={() => setShowForm(false)}
            >
              Cancel
            </button>

            <button type="submit" className="mother-primary-btn">
              Save Registration
            </button>
          </div>
        </form>
      ) : (
        <div className="mothers-table-card">
          <div className="mothers-table-header">
            <h2>Registered Mothers ({mothers.length})</h2>

            <div className="mother-search">
              <Search size={18} />
              <input
                placeholder="Search mothers..."
                value={search}
                onChange={(event) => setSearch(event.target.value)}
              />
            </div>
          </div>

          {filteredMothers.length === 0 ? (
            <div className="mothers-empty">
              <Users size={52} />
              <h3>No mothers found</h3>
              <p>
                Register a mother to begin managing maternal
                healthcare records.
              </p>
            </div>
          ) : (
            <div className="mothers-table-wrapper">
              <table>
                <thead>
                  <tr>
                    <th>Full Name</th>
                    <th>Phone</th>
                    <th>County</th>
                    <th>Facility</th>
                    <th>Registration Date</th>
                  </tr>
                </thead>

                <tbody>
                  {filteredMothers.map((mother) => (
                    <tr key={mother.id}>
                      <td>{mother.fullName}</td>
                      <td>{mother.phoneNumber || "—"}</td>
                      <td>{mother.county || "—"}</td>
                      <td>{mother.healthFacility || "—"}</td>
                      <td>{mother.registeredAt ? new Date(mother.registeredAt).toLocaleDateString() : "—"}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      )}
    </div>
  );
}

