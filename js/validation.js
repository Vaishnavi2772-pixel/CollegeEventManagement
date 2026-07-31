const validateEmail = (email) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
const validatePhone = (phone) => /^\d{10}$/.test(phone);
const validatePassword = (password) => password.length >= 8;

function showMessage(message, type = 'info') {
  const existing = document.querySelector('.form-message');
  if (existing) existing.remove();

  const node = document.createElement('div');
  node.className = `form-message ${type}`;
  node.textContent = message;
  document.querySelector('form')?.insertAdjacentElement('afterend', node);
}

function clearMessages() {
  document.querySelectorAll('.form-message').forEach((el) => el.remove());
}
