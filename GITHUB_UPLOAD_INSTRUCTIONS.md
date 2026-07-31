# GitHub Upload Instructions

## 1. Initialize Git
Run the following commands in the project root:

```bash
git init
git add .
git commit -m "Initial commit"
```

## 2. Create a GitHub Repository
- Open GitHub and create a new repository.
- Do not initialize it with a README if you already have one.

## 3. Connect Remote
```bash
git remote add origin https://github.com/<your-username>/<your-repo-name>.git
git branch -M main
git push -u origin main
```

## 4. Optional: Add a .gitignore
If needed, add a .gitignore to exclude editor files and build artifacts.

## 5. Keep the Project Updated
Use:

```bash
git add .
git commit -m "Update project"
git push
```
