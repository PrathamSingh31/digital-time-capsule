import React, { useState, useEffect } from "react";
import axiosPrivate from "../api/axiosPrivate";
import styles from "./FilterMessages.module.css";

export default function FilterMessages({ onMessagesUpdate }) {
  const [year, setYear] = useState("");
  const [sort, setSort] = useState("asc");
  const [years, setYears] = useState([]);

  useEffect(() => {
    // Fetch all messages to extract unique years
    const fetchYears = async () => {
      try {
        const res = await axiosPrivate.get("/api/user/messages");
        const allYears = [...new Set(res.data.map(msg => msg.year))];
        setYears(allYears.sort((a, b) => a - b));
      } catch (error) {
        console.error("Failed to load messages for year filter:", error);
      }
    };
    fetchYears();
  }, []);

  const handleFilter = async () => {
    try {
      const params = {};
      if (year) params.year = year;
      if (sort) params.sort = sort;

      const res = await axiosPrivate.get("/api/user/messages/filter", { params });
      onMessagesUpdate(res.data);
    } catch (error) {
      console.error("Failed to filter messages:", error);
    }
  };

  return (
    <div className={styles.filterContainer}>
      <select value={year} onChange={(e) => setYear(e.target.value)} className={styles.select}>
        <option value="">All Years</option>
        {years.map(y => <option key={y} value={y}>{y}</option>)}
      </select>

      <select value={sort} onChange={(e) => setSort(e.target.value)} className={styles.select}>
        <option value="asc">Oldest First</option>
        <option value="desc">Newest First</option>
      </select>

      <button onClick={handleFilter} className={styles.button}>Apply</button>
    </div>
  );
}
