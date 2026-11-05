import "./StaffLogin.css";
import { useState } from "react";
import { setApiCredentials, clearApiCredentials, getMothers } from "../services/api";

export default function StaffLogin({ onLogin }) {
  const [username, setUsername] = useState("user");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  async function handleSubmit(event) {
    event.preventDefault();
    setLoading(true);
    setError("");

    try {
      setApiCredentials(username, password);

      // Verify credentials against Spring Boot.
      await getMothers();

      setPassword("");
      onLogin(username);
    } catch (err) {
      clearApiCredentials();
      console.error("MOH 216 login error:", err);
      setError(err.message || "Login failed. Please try again.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="moh-login-page" style={{
      minHeight: "100vh",
      display: "grid",
      placeItems: "center",
      background: "#f1f5f9",
      padding: "24px"
    }}>
      <form className="moh-login-card" onSubmit={handleSubmit} style={{
        width: "100%",
        maxWidth: "400px",
        background: "#ffffff",
        padding: "36px",
        borderRadius: "16px",
        boxShadow: "0 12px 40px rgba(0,0,0,0.08)"
      }}>
        <h1 style={{ color: "#0f766e", marginBottom: "8px" }}>
          MOH 216
        </h1>

        <h2>Staff Login</h2>

        <p style={{ color: "#64748b", marginBottom: "24px" }}>
          Mother & Child Health Handbook
        </p>

        <label htmlFor="staff-username">Username</label>
        <input
          id="staff-username"
          type="text"
          value={username}
          onChange={(event) => setUsername(event.target.value)}
          autoComplete="username"
          required
          style={{ width: "100%", padding: "12px", margin: "8px 0 18px" }}
        />

        <label htmlFor="staff-password">Password</label>
        <input
          id="staff-password"
          type="password"
          value={password}
          onChange={(event) => setPassword(event.target.value)}
          autoComplete="current-password"
          required
          style={{ width: "100%", padding: "12px", margin: "8px 0 18px" }}
        />

        {error && (
          <p role="alert" style={{ color: "#dc2626" }}>{error}</p>
        )}

        <button
          type="submit"
          disabled={loading}
          style={{
            width: "100%",
            padding: "13px",
            background: "#0f766e",
            color: "white",
            border: "none",
            borderRadius: "8px",
            cursor: "pointer"
          }}
        >
          {loading ? "Signing in..." : "Sign In"}
        </button>
      </form>
    </div>
  );
}
