#!/bin/bash
# Script to upload project to GitHub

if [ "$#" -lt 2 ]; then
    echo "Usage: ./upload_to_github.sh GITHUB_USERNAME REPO_NAME"
    echo ""
    echo "Example:"
    echo "  ./upload_to_github.sh pranshu senseway-karnataka"
    exit 1
fi

GITHUB_USERNAME=$1
REPO_NAME=$2
PROJECT_DIR="/Applications/ main_el"

cd "$PROJECT_DIR" || exit 1

echo "🚀 Uploading SenseWay Karnataka to GitHub..."
echo ""

# Check if remote already exists
if git remote | grep -q "^origin$"; then
    echo "⚠️  Remote 'origin' already exists"
    read -p "Do you want to update it? (y/n): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        git remote set-url origin "https://github.com/$GITHUB_USERNAME/$REPO_NAME.git"
    else
        echo "Aborted."
        exit 1
    fi
else
    echo "➕ Adding GitHub remote..."
    git remote add origin "https://github.com/$GITHUB_USERNAME/$REPO_NAME.git"
fi

echo ""
echo "📋 Repository URL: https://github.com/$GITHUB_USERNAME/$REPO_NAME"
echo ""
echo "⚠️  IMPORTANT: Make sure you've created this repository on GitHub first!"
echo ""
read -p "Have you created the repository on GitHub? (y/n): " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo ""
    echo "Please create the repository first:"
    echo "1. Go to https://github.com/new"
    echo "2. Repository name: $REPO_NAME"
    echo "3. DO NOT initialize with README, .gitignore, or license"
    echo "4. Click 'Create repository'"
    echo ""
    echo "Then run this script again."
    exit 1
fi

echo ""
echo "📤 Pushing to GitHub..."
echo "You may be prompted for your GitHub username and password/token"
echo ""

# Check current branch
CURRENT_BRANCH=$(git branch --show-current)
echo "Current branch: $CURRENT_BRANCH"

# Push to GitHub
if git push -u origin "$CURRENT_BRANCH"; then
    echo ""
    echo "✅ ✅ ✅ SUCCESS! ✅ ✅ ✅"
    echo ""
    echo "🎉 Your project is now on GitHub!"
    echo ""
    echo "📍 Repository URL:"
    echo "   https://github.com/$GITHUB_USERNAME/$REPO_NAME"
    echo ""
    echo "🔗 Clone URL:"
    echo "   https://github.com/$GITHUB_USERNAME/$REPO_NAME.git"
    echo ""
    
    # Try to open in browser
    if [[ "$OSTYPE" == "darwin"* ]]; then
        open "https://github.com/$GITHUB_USERNAME/$REPO_NAME" 2>/dev/null
    fi
else
    echo ""
    echo "❌ Push failed!"
    echo ""
    echo "Common issues:"
    echo "1. Repository doesn't exist on GitHub - create it first"
    echo "2. Authentication failed - use Personal Access Token"
    echo "3. Network issue - check your internet connection"
    echo ""
    echo "For authentication help, see GITHUB_UPLOAD.md"
    exit 1
fi
