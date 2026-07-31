document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('contactForm');
  if (!form) return;

  form.addEventListener('submit', (event) => {
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

    const messages = JSON.parse(localStorage.getItem('contactMessages') || '[]');
    messages.push({ ...data, id: Date.now() });
    localStorage.setItem('contactMessages', JSON.stringify(messages));
    showMessage('Message sent successfully. We will get back to you soon.', 'success');
    form.reset();
  });
});
