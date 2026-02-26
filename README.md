# **AtmosSystem**

**AtmosSystem** is a high-performance, standalone atmospheric simulation engine for Minecraft (NeoForge), designed from the ground up with a focus on architecture, computational efficiency, and realistic gas behavior.

## **🎯 Project Goal**

The goal is to provide a scalable gas simulation system that moves away from traditional "block-by-block" ticking in favor of a much more efficient **Zone-based** approach.

## **🧠 Core Concepts**

### **1. Zone-Based Simulation**

Instead of expensive per-block logic, the system identifies airtight environments using an optimized **flood-fill** algorithm.

* Spaces are grouped into **Atmospheric Zones**.
* Each zone is processed as a single unit for global equilibrium.
* Significantly reduces CPU overhead for large-scale builds.

### **2. 1D Array Flattening & Cache Locality**

To achieve maximum performance, each zone's 3D bounding box is flattened into a **1D primitive array**.

* **Zero Object Overhead:** No `HashMap` or `BlockPos` lookups during the simulation loop.
* **Memory Efficiency:** High cache locality ensures fast data access.
* **Optimized Neighbor Logic:** Spatial relationships are calculated using fast index math.

## **🧪 Simulation Model**

Each atmospheric cell tracks:

* **Pressure & Temperature**
* **Gas Mixtures (Concentration maps)**
* **Static/Solid state flags** (to handle walls and vacuum)

## **🛠 Tech Stack**

* **Java 17/21**
* **NeoForge** (as the integration layer)
* **Independent Core:** The simulation engine is decoupled from Minecraft's internal classes, making it easier to maintain and port.

## **📂 Project Structure**

* `core/` — Gas grid, indexers, and solvers.
* `gas/` — Gas types and mixture logic.
* `zone/` — Room detection and flood-fill implementation.

## **🚧 Development Status**

* [x] Architectural design and data structures.
* [x] Optimized 1D grid indexing.
* [ ] Gas solver (Convection & Diffusion).
* [ ] Zone merging/splitting logic.

## **📜 License**

Distributed under the **MIT License**.
