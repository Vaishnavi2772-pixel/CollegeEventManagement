document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('contactForm');
  if (!form) return;

  form.addEventListener('submit', async (event) => {
    event.preventDefault();
    clearMessages();

    const data = {
      name: document.getElementById('name').value.trim(),
      email: document.getElementById('email').value.trim(),
      subject: document.getElementById('subject').value.trim(),
      message: document.getElementById('message').value.trim()
    };

    if (!data.name || !data.email || !data.subject || !data.message) {
      showMessage('Please fill in all contact fields.', 'error');
      return;
    }

    if (!validateEmail(data.email)) {
      showMessage('Please enter a valid email address.', 'error');
      return;
    }

    try {
      const response = await fetch('http://localhost:8080/api/contact', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
      });
      const result = await response.json();
      if (!response.ok || !result.success) {
        showMessage(result.message || 'Unable to send message.', 'error');
        return;
      }
      showMessage(result.message || 'Message sent successfully. We will get back to you soon.', 'success');
      form.reset();
    } catch (error) {
      showMessage('Unable to reach the server. Please ensure the Java app is running.', 'error');
    }
  });
});
