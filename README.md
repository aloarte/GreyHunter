# GreyHunter

![Coverage](https://img.shields.io/endpoint?url=https://raw.githubusercontent.com/aloarte/GreyHunter/main/badges/coverage.json)
[![Playstore Downloads](https://playbadges.pavi2410.me/badge/downloads?id=com.devalr.greyhunter)](https://play.google.com/store/apps/details?id=com.devalr.greyhunter)
[![Tests](https://github.com/aloarte/GreyHunter/actions/workflows/android_ci.yml/badge.svg)](https://github.com/aloarte/GreyHunter/actions/workflows/android_ci.yml)



> Track your miniatures.
> 
> **Kill the grey shame.**
> 
> Finish what you started.

<p align="center">
  <img src="/captures/screenshot-1.png" width="15%">
  <img src="/captures/screenshot-2.png" width="15%">
  <img src="/captures/screenshot-3.png" width="15%">
  <img src="/captures/screenshot-4.png" width="15%">
  <img src="/captures/screenshot-5.png" width="15%">
  <img src="/captures/screenshot-6.png" width="15%">
</p>

# The problem (grey shame)

We all have it.

A shelf full of unpainted miniatures.
A growing pile of shame.
Projects that never reach 100%.

Grey Hunter helps you track your painting progress and finally finish what you started.


# What is Grey Hunter?

Grey Hunter is an Android app designed to help miniature painters:

- Organize painting projects
- Track individual miniatures
- Visualize progress clearly
- Stay motivated
- Import and export your data safely

No more forgotten units. No more eternal grey armies.

# Features & Demo

<table>
  <tr>
    <td width="60%" valign="top">
      <h3>Home – Overview at a Glance</h3>
      <p>Get an instant overview of all your projects and track their progress visually.</p>
      <p>Quickly access any project or miniature with a single tap and stay focused on what matters most.</p>
      <h4>Main Features</h4>
      <ul>
        <li><strong>Visual progress tracking</strong> for all projects</li>
        <li>Quick navigation to project details</li>
        <li>Clean and intuitive dashboard layout</li>
      </ul>
      <p>Stay organized, stay motivated, and always know what’s next.</p>
    </td>
    <td width="40%" valign="top">
      <img src="captures/record-home.gif" width="100%">
    </td>
  </tr>
</table>

<table>
  <tr>
    <td width="60%" valign="top">
      <h3>Paint Mode</h3>
      <p>Select your miniatures and enter a distraction-free <strong>Zen Mode</strong> to fully focus on painting.</p>
      <p>After each session, you can easily update the miniature’s status and keep your progress accurate and up to date.</p>
      <h4>Key Highlights</h4>
      <ul>
        <li><strong>Select</strong> one or multiple miniatures</li>
        <li>Distraction-free painting session</li>
        <li>Quick status updates after finishing</li>
      </ul>
      <p>Perfect for hobbyists who want structure without losing their creative flow.</p>
    </td>
    <td width="40%" valign="top">
      <img src="captures/record-paint.gif" width="100%">
    </td>
  </tr>
</table>

<table>
  <tr>
    <td width="60%" valign="top">
      <h3>Settings & Customization</h3>
      <p>Make the app truly yours with flexible customization options.</p>
      <h4>Available Options</h4>
      <ul>
        <li><strong>Dark / Light Mode</strong></li>
        <li>Customizable progress bar colors</li>
        <li>Export all data to <strong>CSV</strong></li>
      </ul>
      <p>Simple, clean, and fully under your control.</p>
    </td>
    <td width="40%" valign="top">
      <img src="captures/record-settings.gif" width="100%">
    </td>
  </tr>
</table>

# App download

Download the app from the playstore following this [link](https://play.google.com/store/apps/details?id=com.devalr.greyhunter).

# Tech Stack

- 100% Kotlin
- 100% Jetpack Compose
- Koin
- Navigation 3 API
- Clean architecture
- Modular architecture
- Room
- Datastore

# Architecture

This project is built in **Kotlin** and uses **Koin** as an injection framework, building its views
with **Jetpack Compose**.

The application follows a clean, modular architecture with clearly separated responsibilities across modules:

## Core Modules

| Module | Responsibility | Depends On |
|--------|---------------|------------|
| `app` | Entry point, navigation setup, DI configuration | feature modules |
| `common/domain` | Business logic, domain models, repository contracts | — |
| `common/data` | Repository implementations, Room, DataStore, mappers | common/domain |
| `common/framework` | Shared composables, UI utilities, design system | — |


## Feature Modules

Each feature module depends only on the common modules.

| Module | Responsibility |
|--------|---------------|
| `feature/home` | Home screen UI and ViewModel |
| `feature/createproject` | Project creation flow |
| `feature/createminiature` | Miniature creation flow |
| `feature/projectdetail` | Project details screen |
| `feature/minidetail` | Miniature details screen |
| `feature/startpainting` | Miniature selection before painting |
| `feature/painting` | Painting session workflow |
| `feature/settings` | App settings and customization |

## Visual representation

```mermaid
graph TD

    %% App module
    app[app]

    %% Common modules
    subgraph Common
        domain[common/domain]
        data[common/data]
        framework[common/framework]
    end

    %% Feature modules
    subgraph Features
        createMiniature[feature/createminiature]
        createProject[feature/createproject]
        home[feature/home]
        miniDetail[feature/minidetail]
        painting[feature/painting]
        projectDetail[feature/projectdetail]
        settings[feature/settings]
        startPainting[feature/startpainting]
    end

    %% App depends on features
    app --> createMiniature
    app --> createProject
    app --> home
    app --> miniDetail
    app --> painting
    app --> projectDetail
    app --> settings
    app --> startPainting

    %% Features depend on common modules
    createMiniature --> data
    createProject --> data
    home --> data
    miniDetail --> data
    painting --> data
    projectDetail --> data
    settings --> data
    startPainting --> data
    createMiniature --> framework
    createProject --> framework
    home --> framework
    miniDetail --> framework
    painting --> framework
    projectDetail --> framework
    settings --> framework
    startPainting --> framework

    %% Data layer dependency
    data --> domain
```






