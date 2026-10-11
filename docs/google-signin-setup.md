# Google Sign-In + Magic Link setup

The app now supports Google Sign-In (via Appwrite OAuth2 redirect) and email magic links (one-time sign-in). These flows require external configuration — they won't work end-to-end unless you set up the following.

## Google Sign-In

### 1) Google Cloud Console
1. Go to [Google Cloud Console](https://console.cloud.google.com/).
2. Create/select a project.
3. APIs & Services → OAuth consent screen → configure (External, testing is fine for dev).
4. APIs & Services → Credentials → + Create Credentials → OAuth Client ID → Type "Web application".
5. Authorized redirect URI: copy the Redirect URI from Appwrite (step 2), or construct: `https://fra.cloud.appwrite.io/v1/account/sessions/oauth2/callback/google/tgp`.
6. Save → copy the **Client ID** and **Client Secret**.

### 2) Appwrite Console (Cloud)
1. Go to your Appwrite project `tgp` (fra.cloud.appwrite.io).
2. Auth → Settings → Google (OAuth provider).
3. Enable the provider.
4. Paste the Google **Client ID** and **Client Secret**.
5. Appwrite will show the **Redirect URI** — verify it matches the one in Google Cloud; save.
6. In your Android app, the callback scheme is `appwrite-callback-tgp://oauth`. The app handles this via the MainActivity deep link intent-filter.

### 3) Test
1. Build the APK.
2. On the Auth screen, tap **Continue with Google** → opens browser; complete consent → returns to app → auto-navigates to Home.
3. If the provider isn't enabled in Appwrite, you'll see an error.

## Email Magic Link (one-time sign-in)
1. On the Auth screen (Sign In mode), enter your email and tap **Email me a sign-in link**.
2. Appwrite sends an email containing a magic link.
3. Clicking the link redirects back to `appwrite-callback-tgp://magic-link` with `userId` & `secret` (or cookie params). The app completes the session and navigates to Home.

**Requirement:** Appwrite Cloud must be able to send emails. If SMTP is not configured for your project, the magic-link request will fail with an Appwrite error. Set SMTP in Appwrite Console → Auth → Settings → SMTP, or verify your project's email configuration.

## Callback scheme details
- Scheme: `appwrite-callback-tgp`
- OAuth success: `appwrite-callback-tgp://oauth`
- OAuth failure: `appwrite-callback-tgp://oauth-error`
- Magic link: `appwrite-callback-tgp://magic-link`

These are hardcoded in the app for project `tgp`. Change if the project changes.
