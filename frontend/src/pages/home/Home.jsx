import React, { useState } from 'react'

const Home = () => {

    const [articleUrl, setArticleUrl] = useState("")

    const handleUrlChange = (e) => {setArticleUrl(e.target.value)}

    const handleArticleValidation = async (e) => {
        e.preventDefault()

        try {
            const response = await fetch(`http://localhost:8080/api/graph?articleUrl=${encodeURIComponent(articleUrl)}`)

            if(response.ok) {
                const data = await response.json()
            }
        } catch(error) {
            console.error("Request failed: " + error)
        }
    }

  return (
    <div>
      <form onSubmit={handleArticleValidation}>
        <input type="text" placeholder="Article url" value={articleUrl} onChange={handleUrlChange} required />
        <button type="submit">Validate article</button>
      </form>
    </div>
  )
}

export default Home
