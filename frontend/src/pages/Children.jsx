import { useEffect, useMemo, useState } from "react";
import { ArrowLeft, Baby, Plus, Search } from "lucide-react";
import { getChildren, getMothers, registerChild } from "../services/api";
import "./Children.css";

const emptyForm = {
  fullName: "",
  dateOfBirth: "",
  sex: "",
  motherId: "",
  birthWeightKg: "",
  birthFacility: "",
  neonatalHistory: "",
};

export default function Children() {
  const [children, setChildren] = useState([]);
  const [mothers, setMothers] = useState([]);
  const [search, setSearch] = useState("");
  const [form, setForm] = useState(emptyForm);
  const [showForm, setShowForm] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [selectedChild, setSelectedChild] = useState(null);

  async function loadChildren() {
    setLoading(true);
    try {
      setChildren(await getChildren());
      setError("");
    } catch (requestError) {
      console.error("Failed to load children:", requestError);
      setError("Child records could not be loaded.");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadChildren();
    getMothers()
      .then(setMothers)
      .catch((requestError) => {
        console.error("Failed to load mothers for child registration:", requestError);
        setError("Mother records could not be loaded.");
      });
  }, []);

  const filteredChildren = useMemo(() => {
    const query = search.trim().toLowerCase();
    if (!query) {
      return children;
    }
    return children.filter((child) =>
      `${child.fullName} ${child.motherName}`.toLowerCase().includes(query)
    );
  }, [children, search]);

  function handleChange(event) {
    const { name, value } = event.target;
    setForm((previous) => ({ ...previous, [name]: value }));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setError("");
    setSuccess("");

    try {
      await registerChild({
        ...form,
        motherId: Number(form.motherId),
        birthWeightKg: form.birthWeightKg ? Number(form.birthWeightKg) : null,
        birthFacility: form.birthFacility.trim() || null,
        neonatalHistory: form.neonatalHistory.trim() || null,
      });
      await loadChildren();
      setForm(emptyForm);
      setShowForm(false);
      setSuccess("Child registered successfully.");
    } catch (requestError) {
      console.error("Child registration failed:", requestError);
      setError(requestError.message || "Child registration failed.");
    }
  }

  return (
    <div className="children-page">
      <div className="children-heading">
        <div>
          <h1>Child Registration</h1>
          <p>Maintain child profiles linked to their mother or caregiver.</p>
        </div>
        <button className="child-primary-btn" onClick={() => setShowForm(!showForm)}>
          {showForm ? <ArrowLeft size={18} /> : <Plus size={18} />}
          {showForm ? "Back to Records" : "Register Child"}
        </button>
      </div>

      {error && <p className="children-message error" role="alert">{error}</p>}
      {success && <p className="children-message success" role="status">{success}</p>}

      {selectedChild ? (
        <section className="child-profile-card">
          <button className="child-back-link" onClick={() => setSelectedChild(null)}>
            <ArrowLeft size={17} /> Back to child records
          </button>
          <h2>{selectedChild.fullName}</h2>
          <p className="child-profile-subtitle">Child profile</p>
          <div className="child-profile-grid">
            <div><span>Date of birth</span><strong>{selectedChild.dateOfBirth}</strong></div>
            <div><span>Sex</span><strong>{selectedChild.sex}</strong></div>
            <div><span>Mother or caregiver</span><strong>{selectedChild.motherName}</strong></div>
            <div><span>Birth facility</span><strong>{selectedChild.birthFacility || "Not recorded"}</strong></div>
            <div><span>Birth weight</span><strong>{selectedChild.birthWeightKg ? `${selectedChild.birthWeightKg} kg` : "Not recorded"}</strong></div>
            <div><span>Registered</span><strong>{selectedChild.registeredAt ? new Date(selectedChild.registeredAt).toLocaleDateString() : "—"}</strong></div>
          </div>
          <h3>Neonatal history</h3>
          <p>{selectedChild.neonatalHistory || "No neonatal history recorded."}</p>
        </section>
      ) : showForm ? (
        <form className="child-form" onSubmit={handleSubmit}>
          <h2>Child information</h2>
          <div className="child-form-grid">
            <label>
              Full name *
              <input name="fullName" value={form.fullName} onChange={handleChange} maxLength="150" required />
            </label>
            <label>
              Date of birth *
              <input type="date" name="dateOfBirth" value={form.dateOfBirth} onChange={handleChange}
                max={new Date().toISOString().split("T")[0]} required />
            </label>
            <label>
              Sex *
              <select name="sex" value={form.sex} onChange={handleChange} required>
                <option value="">Select sex</option>
                <option value="Female">Female</option>
                <option value="Male">Male</option>
                <option value="Intersex">Intersex</option>
                <option value="Not recorded">Not recorded</option>
              </select>
            </label>
            <label>
              Mother or caregiver *
              <select name="motherId" value={form.motherId} onChange={handleChange} required>
                <option value="">Select mother</option>
                {mothers.map((mother) => (
                  <option key={mother.id} value={mother.id}>{mother.fullName}</option>
                ))}
              </select>
            </label>
            <label>
              Birth weight (kg)
              <input type="number" name="birthWeightKg" value={form.birthWeightKg} onChange={handleChange}
                min="0.01" step="0.01" />
            </label>
            <label>
              Facility of birth
              <input name="birthFacility" value={form.birthFacility} onChange={handleChange} maxLength="150" />
            </label>
          </div>
          <label>
            Neonatal history
            <textarea name="neonatalHistory" value={form.neonatalHistory} onChange={handleChange} maxLength="2000" rows="4" />
          </label>
          <div className="child-form-actions">
            <button type="button" className="child-cancel-btn" onClick={() => setShowForm(false)}>Cancel</button>
            <button type="submit" className="child-primary-btn">Save Registration</button>
          </div>
        </form>
      ) : (
        <div className="children-table-card">
          <div className="children-table-header">
            <h2>Registered Children ({children.length})</h2>
            <div className="child-search">
              <Search size={18} />
              <input placeholder="Search child or mother..." value={search}
                onChange={(event) => setSearch(event.target.value)} />
            </div>
          </div>
          {loading ? <p className="children-empty">Loading child records...</p> :
            filteredChildren.length === 0 ? (
              <div className="children-empty">
                <Baby size={52} />
                <h3>No children found</h3>
                <p>Register a child to begin managing child health records.</p>
              </div>
            ) : (
              <div className="children-table-wrapper">
                <table>
                  <thead><tr><th>Full name</th><th>Date of birth</th><th>Sex</th><th>Mother</th><th>Birth facility</th><th>Profile</th></tr></thead>
                  <tbody>
                    {filteredChildren.map((child) => (
                      <tr key={child.id}>
                        <td>{child.fullName}</td>
                        <td>{child.dateOfBirth}</td>
                        <td>{child.sex}</td>
                        <td>{child.motherName}</td>
                        <td>{child.birthFacility || "—"}</td>
                        <td><button className="child-view-btn" onClick={() => setSelectedChild(child)}>View profile</button></td>
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
