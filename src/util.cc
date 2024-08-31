#pragma once

#include "root.cc"

#include "../coding/lib.cc"

#include <cmath>

/// @brief Utilities.
/// 
namespace zodiac::util {

    /// @brief A utility for 2-dimensional vectors.
    /// 
    /// Note that in this context, "vector" has nothing to do with `std::vector`.
    /// 
    class Vec2 final {

    public:

        /// @brief The x component.
        /// 
        double x;

        /// @brief The y component.
        /// 
        double y;

        /// @brief Construct the vector (x,y).
        /// @param x the x component
        /// @param y the y component
        /// 
        inline constexpr Vec2(double x, double y) noexcept : x(x), y(y) {}

        /// @brief Vector addition.
        /// @param rhs right-hand side
        /// @return result of addition
        /// 
        inline constexpr auto operator+(Vec2 rhs) const noexcept -> Vec2 {
            return Vec2(this->x + rhs.x, this->y + rhs.y);
        }

        /// @brief Inverse of vector.
        /// @return inversed result
        /// 
        inline constexpr auto operator-() const noexcept -> Vec2 {
            return Vec2(-this->x, -this->y);
        }

        /// @brief Vector subtraction.
        /// @param rhs right-hand side
        /// @return result of subtraction
        /// 
        inline constexpr auto operator-(Vec2 rhs) const noexcept -> Vec2 {
            return *this + (-rhs);
        }

        /// @brief Inner product of vector.
        /// @param rhs right-hand side
        /// @return result of multiplication
        /// 
        inline constexpr auto operator*(Vec2 rhs) const noexcept -> double {
            return this->x * rhs.x + this->y * rhs.y;
        }

        /// @brief Length of the vector.
        /// @return radius
        /// 
        inline constexpr auto len() const noexcept -> double {
            return std::sqrt(this->x * this->x + this->y * this->y);
        }

        /// @brief Direction angle.
        /// @return the direction
        /// 
        inline constexpr auto dir() const noexcept -> double {
            return std::atan2(this->y, this->x);
        }

        /// @brief Construct new vector with specified x.
        /// @param x the new x component
        /// @return the new vector
        /// 
        inline constexpr auto with_x(double x) const noexcept -> Vec2 {
            return Vec2(x, this->y);
        }

        /// @brief Construct new vector with specified y.
        /// @param y the new y component
        /// @return the new vector
        /// 
        inline constexpr auto with_y(double y) const noexcept -> Vec2 {
            return Vec2(this->x, y);
        }

        /// @brief Construct new vector with specified length.
        /// @param len the new length
        /// @return the new vector
        /// 
        inline constexpr auto with_len(double len) const noexcept -> Vec2 {
            auto const rat = len / this->len();
            return Vec2(this->x * rat, this->y * rat);
        }

        /// @brief Construct new vector with specified direction.
        /// @param dir the new direction
        /// @return the new vector
        /// 
        inline constexpr auto with_dir(double dir) const noexcept -> Vec2 {
            return Vec2(this->len() * std::cos(dir), this->len() * std::sin(dir));
        }

        /// @brief Rotate the vector.
        /// @param phi the angle to rotate
        /// @return the rotated vector
        /// 
        inline constexpr auto rot(double phi) const noexcept -> Vec2 {
            return this->with_dir(this->dir() + phi);
        }

    };

}
