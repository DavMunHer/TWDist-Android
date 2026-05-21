# Release pipeline setup (one-time)

Automatic releases run on every push to `main` via [`.github/workflows/release.yml`](../.github/workflows/release.yml). Each release is tagged with the format `YYYY.M.D.N` (e.g. `2026.5.21.3` for the third release on that day).

## 1. Generate the release keystore

Run once on your machine (do **not** commit the `.jks` file):

```sh
keytool -genkey -v \
  -keystore twdist-release.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias twdist-key
```

Encode for GitHub Actions:

```sh
# Linux
base64 -w 0 twdist-release.jks > keystore_b64.txt

# macOS
base64 -i twdist-release.jks -o keystore_b64.txt
```

Store `twdist-release.jks` and passwords in a secure password manager. Losing the keystore prevents shipping updates with the same app signing identity.

## 2. Add GitHub repository secrets

In the repo: **Settings → Secrets and variables → Actions → New repository secret**

| Secret | Value |
|--------|--------|
| `KEYSTORE_BASE64` | Contents of `keystore_b64.txt` |
| `KEYSTORE_STORE_PASSWORD` | Keystore password |
| `KEYSTORE_KEY_ALIAS` | e.g. `twdist-key` |
| `KEYSTORE_KEY_PASSWORD` | Key password |
| `RELEASE_BASE_URL` | Production API URL **with trailing slash**, e.g. `https://api.example.com/api/` |

If `RELEASE_BASE_URL` is missing or empty, the release build fails in CI. A malformed URL (no trailing slash) is normalized at build time; an empty value still crashes the app at startup when Retrofit initializes.

## 3. Verify

Merge or push to `main`. The workflow will build a signed release APK and publish a **pre-release** on GitHub with the APK attached. Lint and tests are enforced by the PR CI workflow before merge.

Download the APK from the release assets page and install on a device (enable install from unknown sources if needed).
