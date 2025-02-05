import React, { useState } from "react";
import "./home.css"

const Home = () => {
  const [articleUrl, setArticleUrl] = useState("");
  const [iptc, setIptc] = useState("");
  const [clickbait, setClickbait] = useState("");
  const [sentiment, setSentiment] = useState("");
  const [trustlevel, setTrustlevel] = useState("");

  const handleUrlChange = (e) => setArticleUrl(e.target.value);

  const handleArticleValidation = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch(
        `http://localhost:8080/api/articleInfo?articleUrl=${encodeURIComponent(
          articleUrl
        )}`
      );

      if (response.ok) {
        const data = await response.json();
        setIptc(data.iptc);
        setClickbait(data.clickbait);
        setSentiment(data.sentiment);
        setTrustlevel(data.trustLevel);
      }
    } catch (error) {
      console.error("Request failed: " + error);
    }
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-50 p-6">
      <div className="bg-white p-8 rounded-xl shadow-lg w-full max-w-lg">
        <h1 className="text-3xl font-bold mb-6 text-center text-blue-600">Article Validator</h1>
        <form onSubmit={handleArticleValidation} className="space-y-5">
          <input
            type="text"
            placeholder="Enter article URL"
            value={articleUrl}
            onChange={handleUrlChange}
            required
            className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
          />
          <button
            type="submit"
            className="w-full bg-blue-500 text-white p-3 rounded-lg hover:bg-blue-600 transition font-semibold"
          >
            Validate Article
          </button>
        </form>
        <div className="mt-8 bg-gray-100 p-5 rounded-lg shadow-inner">
          <h2 className="text-xl font-semibold text-gray-700">Results:</h2>
          <p className="text-gray-700 mt-2">Iptc Score: <span className="font-bold text-blue-500">{iptc}</span></p>
          <p className="text-gray-700">Clickbait Score: <span className="font-bold text-red-500">{clickbait}</span></p>
          <p className="text-gray-700">Sentiment Score: <span className="font-bold text-green-500">{sentiment}</span></p>
          <p className="text-gray-700">Trust Level: <span className="font-bold text-yellow-500">{trustlevel}</span></p>
        </div>
        <div>
          
        </div>
      </div>
    </div>
  );
};

export default Home;